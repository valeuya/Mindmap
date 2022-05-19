package com.itemis.gef.tutorial.mindmap.parts.feedback;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.fx.anchors.IAnchor;
import org.eclipse.gef.fx.anchors.StaticAnchor;
import org.eclipse.gef.geometry.convert.fx.FX2Geometry;
import org.eclipse.gef.geometry.convert.fx.Geometry2FX;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.mvc.fx.parts.AbstractFeedbackPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapConnectionVisual;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Часть, представляющая отзыв о создании соединения, привязка к
 * {@link MindMapNodePart} и положение мыши.
 * 
 */
public class CreateConnectionFeedbackPart extends AbstractFeedbackPart<Node> {

	@Override
	protected Node doCreateVisual() {
		return new MindMapConnectionVisual();
	}

	@Override
	protected void doRefreshVisual(Node visual) {
	}

	@Override
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {
		// найти провайдера привязки, который должен быть зарегистрирован в модуле
		// будьте внимательны при использовании правильные интерфейсы (часто
		// используется Proviser)
		@SuppressWarnings("serial")
		Provider<? extends IAnchor> adapter = anchorage
				.getAdapter(AdapterKey.get(new TypeToken<Provider<? extends IAnchor>>() {
				}));
		if (adapter == null) {
			throw new IllegalStateException("No adapter  found for <" + anchorage.getClass() + "> found.");
		}
		// устанавливаем начальную привязку
		IAnchor anchor = adapter.get();
		getVisual().setStartAnchor(anchor);

		MousePositionAnchor endAnchor = new MousePositionAnchor(
				FX2Geometry.toPoint(getVisual().localToScene(Geometry2FX.toFXPoint(getVisual().getStartPoint()))));
		endAnchor.init();
		getVisual().setEndAnchor(endAnchor);
	}

	@Override
	protected void doDetachFromAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {
		getVisual().setStartPoint(getVisual().getStartPoint());
		((MousePositionAnchor) getVisual().getEndAnchor()).dispose();
		getVisual().setEndPoint(getVisual().getEndPoint());
	}

	@Override
	public MindMapConnectionVisual getVisual() {
		return (MindMapConnectionVisual) super.getVisual();
	}

	private class MousePositionAnchor extends StaticAnchor implements EventHandler<MouseEvent> {

		public MousePositionAnchor(Point referencePositionInScene) {
			super(referencePositionInScene);
		}

		public void init() {
			// прослушиваем любое движение мыши и перемещаем
			getRoot().getVisual().getScene().addEventHandler(MouseEvent.MOUSE_MOVED, this);
		}

		@Override
		public void handle(MouseEvent event) {
			Point v = new Point(event.getSceneX(), event.getSceneY());
			referencePositionProperty().setValue(v);
		}

		public void dispose() {
			// прослушиваем любое движение мыши и перемещаем
			getRoot().getVisual().getScene().removeEventHandler(MouseEvent.MOUSE_MOVED, this);
		}

	}
}
