package com.itemis.gef.tutorial.mindmap.model;

import org.eclipse.gef.geometry.planar.Rectangle;

import javafx.scene.paint.Color;

public class SimpleMindMapExampleFactory {

    private static final double WIDTH = 150;

    public SimpleMindMap createComplexExample() {
        SimpleMindMap mindMap = new SimpleMindMap();

        MindMapNode center = new MindMapNode();
        center.setTitle("Основная идея");
        center.setDescription("Это моя основная идея");
        center.setColor(Color.GREENYELLOW);
        center.setBounds(new Rectangle(250, 50, WIDTH, 100));

        mindMap.addChildElement(center);

        MindMapNode child = null;
        for (int i = 0; i < 5; i++) {
            child = new MindMapNode();
            child.setTitle("Ассоциация #" + i);
            child.setDescription("Я только что понял, что это связано с основной идеей!");
            child.setColor(Color.ALICEBLUE);

            child.setBounds(new Rectangle(50 + (i * 200), 250, WIDTH, 100));
            mindMap.addChildElement(child);

            MindMapConnection conn = new MindMapConnection();
            conn.connect(center, child);
            mindMap.addChildElement(conn);
        }

        MindMapNode child2 = new MindMapNode();
        child2.setTitle("Ассоциация  #4-2");
        child2.setDescription("Я только что понял, что это связано с последней идеей!");
        child2.setColor(Color.LIGHTGRAY);
        child2.setBounds(new Rectangle(250, 550, WIDTH, 100));
        mindMap.addChildElement(child2);

        MindMapConnection conn = new MindMapConnection();
        conn.connect(child, child2);
        mindMap.addChildElement(conn);

        return mindMap;
    }

    public SimpleMindMap createSingleNodeExample() {
        SimpleMindMap mindMap = new SimpleMindMap();

        MindMapNode center = new MindMapNode();
        center.setTitle("Основная идея");
        center.setDescription("то моя основная идея. Мне нужно более подробное объяснение, чтобы я мог протестировать искажение.");
        center.setColor(Color.GREENYELLOW);
        center.setBounds(new Rectangle(20, 50, WIDTH, 100));

        mindMap.addChildElement(center);

        return mindMap;
    }
}