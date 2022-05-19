package com.itemis.gef.tutorial.mindmap.policies;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.policies.CreationPolicy;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.operations.ChangeSelectionOperation;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultimap;
import com.google.common.reflect.TypeToken;
import com.itemis.gef.tutorial.mindmap.model.MindMapConnection;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel.Type;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Обработчик для создания нового узла с использованием {@link ItemCreationModel
 * 
 * @author hniederhausen
 *
 */
public class CreateNewConnectiononClickHandler extends AbstractHandler implements IOnClickHandler {

	@SuppressWarnings("serial")
	@Override
	public void click(MouseEvent e) {
		if (!e.isPrimaryButtonDown()) {
			return;
		}

		IViewer viewer = getHost().getRoot().getViewer();
		ItemCreationModel creationModel = viewer.getAdapter(ItemCreationModel.class);
		if (creationModel.getType() != Type.Connection) {
			return; // не нужно создавать соединение
		}

		if (creationModel.getSource() == null) {
			// хост является источником
			creationModel.setSource((MindMapNodePart) getHost());
			return; // ждем следующего клика
		}

		// хорошо, у нас есть пара
		MindMapNodePart source = creationModel.getSource();
		MindMapNodePart target = (MindMapNodePart) getHost();

		// проверяем правильность
		if (source == target) {
			return;
		}

		IVisualPart<? extends Node> part = getHost().getRoot().getChildrenUnmodifiable().get(0);
		if (part instanceof SimpleMindMapPart) {
			MindMapConnection newConn = new MindMapConnection();
			newConn.connect(source.getContent(), target.getContent());

			// GEF предоставляет CreatePolicy и операции для добавления
			// нового элемента в модель
			IRootPart<? extends Node> root = getHost().getRoot();
			// получить политику, связанную с IRootPart
			CreationPolicy creationPolicy = root.getAdapter(new TypeToken<CreationPolicy>() {
			});
			// инициализируем политику
			init(creationPolicy);
			// создаем IContentPart для нашей новой модели. Мы не используем
			// возвращенная часть контента
			creationPolicy.create(newConn, (SimpleMindMapPart) part,
					HashMultimap.<IContentPart<? extends Node>, String>create());
			// выполнить фиксацию
			commit(creationPolicy);
			// выбор целевого узла 
			try {
				viewer.getDomain().execute(new ChangeSelectionOperation(viewer, Collections.singletonList(target)),
						null);
			} catch (ExecutionException e1) {
			}
		}

		// окончательно сбрасываем createModel 
		creationModel.setSource(null);
		creationModel.setType(Type.None);
	}

}
