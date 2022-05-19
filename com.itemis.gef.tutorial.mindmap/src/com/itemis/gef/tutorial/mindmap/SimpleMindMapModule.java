package com.itemis.gef.tutorial.mindmap;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.common.adapt.inject.AdapterMaps;
import org.eclipse.gef.mvc.fx.MvcFxModule;
import org.eclipse.gef.mvc.fx.behaviors.HoverBehavior;
import org.eclipse.gef.mvc.fx.behaviors.SelectionBehavior;
import org.eclipse.gef.mvc.fx.handlers.FocusAndSelectOnClickHandler;
import org.eclipse.gef.mvc.fx.handlers.HoverOnHoverHandler;
import org.eclipse.gef.mvc.fx.handlers.ResizeTranslateFirstAnchorageOnHandleDragHandler;
import org.eclipse.gef.mvc.fx.handlers.TranslateSelectedOnDragHandler;
import org.eclipse.gef.mvc.fx.parts.DefaultHoverFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultSelectionFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultSelectionHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.SquareSegmentHandlePart;
import org.eclipse.gef.mvc.fx.policies.ResizePolicy;
import org.eclipse.gef.mvc.fx.policies.TransformPolicy;
import org.eclipse.gef.mvc.fx.providers.ShapeBoundsProvider;
import org.eclipse.gef.mvc.fx.providers.ShapeOutlineProvider;

import com.google.inject.multibindings.MapBinder;
import com.itemis.gef.tutorial.mindmap.behaviors.CreateFeedbackBehavior;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapPartsFactory;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapAnchorProvider;
import com.itemis.gef.tutorial.mindmap.parts.feedback.CreateFeedbackPartFactory;
import com.itemis.gef.tutorial.mindmap.parts.handles.MindMapSelectionHandlePartFactory;
import com.itemis.gef.tutorial.mindmap.parts.handles.DeleteMindMapNodeHandlePart;
import com.itemis.gef.tutorial.mindmap.parts.handles.MindMapHoverIntentHandlePartFactory;
import com.itemis.gef.tutorial.mindmap.policies.CreateNewConnectiononClickHandler;
import com.itemis.gef.tutorial.mindmap.policies.CreateNewNodeOnClickHandler;
import com.itemis.gef.tutorial.mindmap.policies.DeleteNodeOnHandleClickHandler;
import com.itemis.gef.tutorial.mindmap.policies.DeletionPolicyEx;
import com.itemis.gef.tutorial.mindmap.policies.ShowMindMapNodeContextMenuOnClickPolicy;

/**
 * The Guice Module to configure our parts and behaviors.
 *
 */
public class SimpleMindMapModule extends MvcFxModule {

	@Override
	protected void bindIContentPartFactoryAsContentViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// ��������� ������� MindMapPartsFactory � �������� ��������� �����������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(MindMapPartsFactory.class);
	}

	/**
	 *
	 * @param adapterMapBinder
	 */
	protected void bindMindMapNodePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// ��������� ���������� ��������, ������������� ��� �������� �������� ����������

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SimpleMindMapAnchorProvider.class);

		// ����������� ��������� ���������, ������� ������������ � ����� ����������
		// ��������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ShapeOutlineProvider.class);

		// ������������ �������� ����� ��� ��������� �� �����, ������������
		// HoverBehavior
		AdapterKey<?> role = AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER);
		adapterMapBinder.addBinding(role).to(ShapeOutlineProvider.class);

		// ������������ �������� ����� ������ ��� ������
		role = AdapterKey.role(DefaultSelectionFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER);
		adapterMapBinder.addBinding(role).to(ShapeBoundsProvider.class);

		// ��������� ����������� ����� � ������� �������������� �����
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TranslateSelectedOnDragHandler.class);

		// specify the factory to create the geometry object for the selection
		// handles
		role = AdapterKey.role(DefaultSelectionHandlePartFactory.SELECTION_HANDLES_GEOMETRY_PROVIDER);
		adapterMapBinder.addBinding(role).to(ShapeBoundsProvider.class);

		// support resizing nodes
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		// �������� ������� ���������� ����������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateNewConnectiononClickHandler.class);

		// ��������� �������� ������������ ���� � �����
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ShowMindMapNodeContextMenuOnClickPolicy.class);
	}

	@Override
	protected void bindAbstractContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractContentPartAdapters(adapterMapBinder);

		// �������� HoverOnHoverPolicy � ������ �����
		// ���� ���� ������������ ��� ������, ��� ��������������� � ��������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);

		// ��������� �������� ������ � ������ � ������ �����, ����������� ������
		// � ������� ������ ������ � ������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FocusAndSelectOnClickHandler.class);
	}

	@Override
	protected void bindIRootPartAdaptersForContentViewer(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindIRootPartAdaptersForContentViewer(adapterMapBinder);

		// �������� Hover Behavior � �������� �����. �� ����� �����������
		// �� ��������� HoverModel � ��������������� �����, ����������� ��� ���������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverBehavior.class);

		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateNewNodeOnClickHandler.class);

		// ���������� ��������� �������� ����� ��� ��������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateFeedbackBehavior.class);

	}

	/**
	 * ����������� ����� �������� ��������� (���������� � ����) � ���������
	 * 
	 * @param adapterMapBinder
	 */
	protected void bindSquareSegmentHandlePartPartAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole())
				.to(ResizeTranslateFirstAnchorageOnHandleDragHandler.class);
	}

	@Override
	protected void bindIViewerAdaptersForContentViewer(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindIViewerAdaptersForContentViewer(adapterMapBinder);
		// ����������� ������ � �������� ��������� �����������
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ItemCreationModel.class);

		// �������� ������� ������� �������� ����� �� �������� � ������� ����, ���������
		// � ���������
		AdapterKey<?> role = AdapterKey.role(CreateFeedbackBehavior.CREATE_FEEDBACK_PART_FACTORY);
		adapterMapBinder.addBinding(role).to(CreateFeedbackPartFactory.class);
	}

	@Override
	protected void bindSelectionHandlePartFactoryAsContentViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// ��������������� ������� �� ��������� ����� �����������
		AdapterKey<?> role = AdapterKey.role(SelectionBehavior.SELECTION_HANDLE_PART_FACTORY);
		adapterMapBinder.addBinding(role).to(MindMapSelectionHandlePartFactory.class);
	}

	@Override
	protected void bindHoverHandlePartFactoryAsContentViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// ��������������� ������� �� ��������� ����� �����������
		AdapterKey<?> role = AdapterKey.role(HoverBehavior.HOVER_HANDLE_PART_FACTORY);
		adapterMapBinder.addBinding(role).to(MindMapHoverIntentHandlePartFactory.class);
	}

	protected void bindDeleteMindMapNodeHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DeleteNodeOnHandleClickHandler.class);
	}
	@Override 
	protected void bindDeletionPolicyAsIRootPartAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) { 
	    adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DeletionPolicyEx.class); 
	}

	@Override
	protected void configure() {
		// ��������� ������������ �� ���������
		super.configure();

		bindMindMapNodePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), MindMapNodePart.class));

		// � ������� ���� �������� �� ������� �����������
		bindSquareSegmentHandlePartPartAdapter(
				AdapterMaps.getAdapterMapBinder(binder(), SquareSegmentHandlePart.class));

		bindDeleteMindMapNodeHandlePartAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), DeleteMindMapNodeHandlePart.class));
	}

}
