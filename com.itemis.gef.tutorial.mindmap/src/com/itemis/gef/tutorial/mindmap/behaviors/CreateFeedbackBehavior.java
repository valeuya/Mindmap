package com.itemis.gef.tutorial.mindmap.behaviors;

import org.eclipse.gef.mvc.fx.behaviors.AbstractBehavior;
import org.eclipse.gef.mvc.fx.parts.IFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;

/**
 * The behavior is listening to changes in the {@link ItemCreationModel} and creates a connection feedback if necessary.
 * 
 */
public class CreateFeedbackBehavior extends AbstractBehavior {

	/**
	 * ���� �������� ��� {@link IFeedbackPartFactory}, ������� ������������ 
	 * ��� �������� ������ �������� ����� ��� ���������. 
	 */
	public static final String CREATE_FEEDBACK_PART_FACTORY = "CREATE_FEEDBACK_PART_FACTORY";

	@Override
	protected void doActivate() {

		ItemCreationModel model = getHost().getRoot().getViewer().getAdapter(ItemCreationModel.class);
		model.getSourceProperty().addListener((o, oldVal, newVal) -> {
			if (newVal == null) {
				clearFeedback(); // �������� ��� �� ����������, ������� ��� �������� ����� 
			} else {
				addFeedback(newVal);  // �� ���� ��������, ������ ����� 
			}
		});

		super.doActivate();
	}

	@Override
	protected IFeedbackPartFactory getFeedbackPartFactory(IViewer viewer) {
		return getFeedbackPartFactory(viewer, CREATE_FEEDBACK_PART_FACTORY);
	}

}
