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
 * ���������� ��� �������� ������ ���� � �������������� {@link ItemCreationModel
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
			return; // �� ����� ��������� ����������
		}

		if (creationModel.getSource() == null) {
			// ���� �������� ����������
			creationModel.setSource((MindMapNodePart) getHost());
			return; // ���� ���������� �����
		}

		// ������, � ��� ���� ����
		MindMapNodePart source = creationModel.getSource();
		MindMapNodePart target = (MindMapNodePart) getHost();

		// ��������� ������������
		if (source == target) {
			return;
		}

		IVisualPart<? extends Node> part = getHost().getRoot().getChildrenUnmodifiable().get(0);
		if (part instanceof SimpleMindMapPart) {
			MindMapConnection newConn = new MindMapConnection();
			newConn.connect(source.getContent(), target.getContent());

			// GEF ������������� CreatePolicy � �������� ��� ����������
			// ������ �������� � ������
			IRootPart<? extends Node> root = getHost().getRoot();
			// �������� ��������, ��������� � IRootPart
			CreationPolicy creationPolicy = root.getAdapter(new TypeToken<CreationPolicy>() {
			});
			// �������������� ��������
			init(creationPolicy);
			// ������� IContentPart ��� ����� ����� ������. �� �� ����������
			// ������������ ����� ��������
			creationPolicy.create(newConn, (SimpleMindMapPart) part,
					HashMultimap.<IContentPart<? extends Node>, String>create());
			// ��������� ��������
			commit(creationPolicy);
			// ����� �������� ���� 
			try {
				viewer.getDomain().execute(new ChangeSelectionOperation(viewer, Collections.singletonList(target)),
						null);
			} catch (ExecutionException e1) {
			}
		}

		// ������������ ���������� createModel 
		creationModel.setSource(null);
		creationModel.setType(Type.None);
	}

}
