package com.itemis.gef.tutorial.mindmap.parts;

import org.eclipse.gef.common.adapt.IAdaptable;
import org.eclipse.gef.fx.anchors.DynamicAnchor;
import org.eclipse.gef.fx.anchors.DynamicAnchor.AnchorageReferenceGeometry;
import org.eclipse.gef.fx.anchors.IAnchor;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapConnectionVisual;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;

/**
 * The {@link SimpleMindMapAnchorProvider} create an anchor for a
 * {@link MindMapConnectionVisual}.
 *
 * It is bound to an {@link MindMapNodePart} and will be called in the
 * {@link MindMapConnectionPart}.
 *
 */
public class SimpleMindMapAnchorProvider extends IAdaptable.Bound.Impl<IVisualPart<? extends Node>>
		implements Provider<IAnchor> {

	// €корь, если мы уже создали один
	private DynamicAnchor anchor;

	@Override
	public ReadOnlyObjectProperty<IVisualPart<? extends Node>> adaptableProperty() {
		return null;
	}

	@Override
	public IAnchor get() {
		if (anchor == null) {
			// получаем визуализацию от хоста (MindMapNodePart)
			Node anchorage = getAdaptable().getVisual();
			// создаем новый экземпл€р
			anchor = new DynamicAnchor(anchorage);

			// прив€зка ссылки прив€зки к прив€зке объекта, котора€
			// пересчитывает геометрию, когда границы макета
			// прив€зки измен€ютс€
			anchor.getComputationParameter(AnchorageReferenceGeometry.class).bind(new ObjectBinding<IGeometry>() {
				{
					bind(anchorage.layoutBoundsProperty());
				}

				@Override
				protected IGeometry computeValue() {
					@SuppressWarnings("serial")
					// получаем зарегистрированный поставщик геометрии с хоста
					Provider<IGeometry> geomProvider = getAdaptable().getAdapter(new TypeToken<Provider<IGeometry>>() {
					});
					return geomProvider.get();
				}
			});
		}
		return anchor;
	}
}