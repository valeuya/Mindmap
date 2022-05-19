package com.itemis.gef.tutorial.mindmap.models;

import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * {@link ItemCreationModel} используется для хранения состояния создания в приложении.
 * 
 */
public class ItemCreationModel {

	public enum Type {
		None,
		Node,
		Connection
	};
	
	private ObjectProperty<Type> typeProperty = new SimpleObjectProperty<ItemCreationModel.Type>(Type.None);

	private ObjectProperty<MindMapNodePart> sourceProperty = new SimpleObjectProperty<>();

	public ObjectProperty<Type> getTypeProperty() {
		return typeProperty;
	}

	public Type getType() {
		return typeProperty.getValue();
	}

	public void setType(Type type) {
		this.typeProperty.setValue(type);
	}

	public void setSource(MindMapNodePart source) {
		this.sourceProperty.setValue(source);;
	}
	
	public MindMapNodePart getSource() {
		return sourceProperty.getValue();
	}
	
	public ObjectProperty<MindMapNodePart> getSourceProperty() {
		return sourceProperty;
	}
	
}