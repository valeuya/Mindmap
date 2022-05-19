package com.itemis.gef.tutorial.mindmap.policies;

import java.util.ArrayList;

import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.DeletionPolicy;

import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * �������� ������ ��� DeleteMindMapNodeHandlePart.
 *
 * @author hniederhausen
 *
 */
public class DeleteNodeOnHandleClickHandler extends AbstractHandler implements IOnClickHandler {

	@Override
	public void click(MouseEvent e) {
		if (!e.isPrimaryButtonDown()) {
			return;
		}

		//// ���������� ����� ����, ��� ������� ������� ���������� �������� ���
		//// ���������
		MindMapNodePart node = (MindMapNodePart) getHost().getAnchoragesUnmodifiable().keySet().iterator().next();

		// �������������� �������� ��������
		IRootPart<? extends Node> root = getHost().getRoot();
		DeletionPolicy delPolicy = root.getAdapter(DeletionPolicy.class);
		init(delPolicy);

		init(delPolicy);

		// �, �������, ������� ����� ����
		delPolicy.delete(node);
		commit(delPolicy);
	}

}
