package com.itemis.gef.tutorial.mindmap.parts.handles;

import java.util.Iterator;

import org.eclipse.gef.mvc.fx.parts.AbstractHandlePart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class DeleteMindMapNodeHandlePart extends AbstractHandlePart<Group> {

    @Override
    protected Group doCreateVisual() {
        Group group = new Group();

        // ������� ������������ �����
        double SIZE = 5;
        Polyline topLeftBottomRight = new Polyline(-SIZE, -SIZE, SIZE, SIZE);
        Polyline topRightBottomLeft = new Polyline(SIZE, -SIZE, -SIZE, SIZE);

        // ����������� ��: �������, �������, ������� ��������� ����� 
        Color stroke = Color.RED;
        double strokeWidth = 5d;
        StrokeLineCap lineCap = StrokeLineCap.ROUND;
        for (Polyline pl : new Polyline[] { topLeftBottomRight, topRightBottomLeft }) {
            pl.setStroke(stroke);
            pl.setStrokeWidth(strokeWidth);
            pl.setStrokeLineCap(lineCap);
        }

        // ���������� ������������ ����� � ������ X 
        group.getChildren().addAll(topLeftBottomRight, topRightBottomLeft);

     // ����������� ���������
        group.setOpacity(0d);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), group);
        fadeIn.setFromValue(0d);
        fadeIn.setToValue(1d);
        fadeIn.play();

        // ������� ������������ 
        group.setPickOnBounds(true);

        return group;
    }

    @Override
    protected void doRefreshVisual(Group visual) {
        Iterator<IVisualPart<? extends Node>> iterator = getAnchoragesUnmodifiable().keySet().iterator();
        if (!iterator.hasNext()) {
            return;
        }
     // ������� X � ������ ������� ���� 
        double PAD = 1;
        IVisualPart<? extends Node> host = iterator.next();
        Bounds hostBounds = host.getVisual().getBoundsInParent();
        visual.relocate(hostBounds.getMaxX() + PAD,
                hostBounds.getMinY() - visual.getBoundsInParent().getHeight() - PAD);
    }

}
