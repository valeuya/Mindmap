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
 * Политика кликов для DeleteMindMapNodeHandlePart.
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

		//// определяем часть узла, для которой щелкнут дескриптор удаления при
		//// наведении
		MindMapNodePart node = (MindMapNodePart) getHost().getAnchoragesUnmodifiable().keySet().iterator().next();

		// инициализируем политику удаления
		IRootPart<? extends Node> root = getHost().getRoot();
		DeletionPolicy delPolicy = root.getAdapter(DeletionPolicy.class);
		init(delPolicy);

		init(delPolicy);

		// и, наконец, удалить часть узла
		delPolicy.delete(node);
		commit(delPolicy);
	}

}
