package com.itemis.gef.tutorial.mindmap.policies;

import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.policies.CreationPolicy;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultimap;
import com.google.common.reflect.TypeToken;
import com.itemis.gef.tutorial.mindmap.model.MindMapNode;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel.Type;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Обработчик, который прослушивает первичные клики и создает новый узел, если
 * {@link ItemCreationModel} находится в правильном состоянии.
 */
public class CreateNewNodeOnClickHandler extends AbstractHandler implements IOnClickHandler {

	@SuppressWarnings("serial")
	@Override
	public void click(MouseEvent e) {
		if (!e.isPrimaryButtonDown()) {
			return; // неправильная кнопка мыши
		}

		IViewer viewer = getHost().getRoot().getViewer();
		ItemCreationModel creationModel = viewer.getAdapter(ItemCreationModel.class);
		if (creationModel == null) {
			throw new IllegalStateException("No ItemCreationModel bound to viewer!");
		}

		if (creationModel.getType() != Type.Node) {
			// не нужно создавать узел
			return;
		}
		IVisualPart<? extends Node> part = viewer.getRootPart().getChildrenUnmodifiable().get(0);

		if (part instanceof SimpleMindMapPart) {
			// вычисляем координаты мыши
			// определяем координаты начала новых узлов в координатах модели
			Point2D mouseInLocal = part.getVisual().sceneToLocal(e.getSceneX(), e.getSceneY());

			MindMapNode newNode = new MindMapNode();
			newNode.setTitle("New node");
			newNode.setDescription("no description");
			newNode.setColor(Color.GREENYELLOW);
			newNode.setBounds(new Rectangle(mouseInLocal.getX(), mouseInLocal.getY(), 120, 80));

			// GEF предоставляет CreatePolicy и операции для добавления
			// нового элемента в модель
			IRootPart<? extends Node> root = getHost().getRoot();
			// получить политику, связанную с IRootPart
			CreationPolicy creationPolicy = root.getAdapter(CreationPolicy.class);
			// инициализируем политику
			init(creationPolicy);
			// создаем IContentPart для нашей новой модели. Мы не используем возвращаемую 
			// часть контента
			creationPolicy.create(newNode, (SimpleMindMapPart) part,
					HashMultimap.<IContentPart<? extends Node>, String>create());
			// выполнить 
			commit(creationPolicy);
		}

		// очищаем выбор 
		creationModel.setType(Type.None);

	}
}
