package com.itemis.gef.tutorial.mindmap.parts.feedback;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.IFeedbackPart;
import org.eclipse.gef.mvc.fx.parts.IFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

/**
 * Фабрика используется в {@link CreateFeedbackBehavior} для создания частей 
 * обратной связи.  
 * 
 */
public class CreateFeedbackPartFactory implements IFeedbackPartFactory {

	@Inject
	Injector injector;
	
	@Override
	public List<IFeedbackPart<? extends Node>> createFeedbackParts(
			List<? extends IVisualPart<? extends Node>> targets,
			Map<Object, Object> contextMap) {
		
		List<IFeedbackPart<? extends Node>> parts = Lists.newArrayList();
		
		if (targets.isEmpty())
			return parts;  // не должно происходить, просто для уверенности
		
		// мы просто ожидаем одну цел
		IVisualPart< ? extends Node> target = targets.get(0);
		
		if (target instanceof MindMapNodePart) {
			// цель MindMapNode является источником соединения, поэтому мы создаем обратную связь соединения 
			CreateConnectionFeedbackPart part = injector.getInstance(CreateConnectionFeedbackPart.class);
			parts.add(part);
		}

		return parts;
	}

}
