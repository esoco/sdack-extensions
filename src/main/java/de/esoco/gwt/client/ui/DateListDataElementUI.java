//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'sdack-extensions' project.
// Copyright 2017 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package de.esoco.gwt.client.ui;

import de.esoco.data.element.DateDataElement;
import de.esoco.data.element.DateListDataElement;
import de.esoco.data.element.DateListDataElement.InteractionType;
import de.esoco.data.element.DateListDataElement.TimetableDisplayStyle;

import de.esoco.ewt.build.ContainerBuilder;
import de.esoco.ewt.build.TimetableBuilder;
import de.esoco.ewt.component.Component;
import de.esoco.ewt.component.Timetable;
import de.esoco.ewt.component.Timetable.TimetableStyle;
import de.esoco.ewt.event.EWTEvent;
import de.esoco.ewt.event.EventType;
import de.esoco.ewt.style.StyleData;

import de.esoco.lib.property.HasProperties;
import de.esoco.lib.property.InteractionEventType;
import de.esoco.lib.property.StandardProperties;
import de.esoco.lib.property.StringProperties;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static de.esoco.data.element.DateListDataElement.TIMETABLE_DAYS;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_DAY_START;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_DISPLAY_STYLE;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_FIRST_WORKING_HOUR;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_HOUR_SUBDIVISIONS;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_HOUR_SUBDIVISION_HEIGHT;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_SCROLL_TO_HOUR;
import static de.esoco.data.element.DateListDataElement.TIMETABLE_SHOW_WEEK_NUMBERS;

import static de.esoco.lib.property.StandardProperties.DATE;


/********************************************************************
 * The user interface implementation for date data elements.
 *
 * @author eso
 */
public class DateListDataElementUI extends DataElementUI<DateListDataElement>
{
	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected Component createInputUI(ContainerBuilder<?>		rBuilder,
									  StyleData					rInputStyle,
									  final DateListDataElement rDataElement)
	{
		@SuppressWarnings("boxing")
		int nScrollToHour =
			rDataElement.getProperty(TIMETABLE_SCROLL_TO_HOUR, -1);

		final Timetable aTimetable =
			TimetableBuilder.addTimetable(rBuilder,
										  rInputStyle,
										  null,
										  nScrollToHour);

		updateTimetable(aTimetable, rDataElement);

		return aTimetable;
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected DataElementInteractionHandler<DateListDataElement> createInteractionHandler(
		DataElementPanelManager rPanelManager,
		DateListDataElement		rDataElement)
	{
		return new DateListInteractionHandler(rPanelManager, rDataElement);
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected void transferDataElementValueToComponent(
		DateListDataElement rElement,
		Component			rComponent)
	{
		if (rComponent instanceof Timetable)
		{
			updateTimetable((Timetable) rComponent, rElement);
		}
		else
		{
			super.transferDataElementValueToComponent(rElement, rComponent);
		}
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected void transferInputToDataElement(
		Component			rComponent,
		DateListDataElement rElement)
	{
		if (rComponent instanceof Timetable)
		{
			Timetable rTimetable = (Timetable) rComponent;

			rElement.setSelection(rTimetable.getSelectedEvent());
			rElement.setProperty(DATE, rTimetable.getDate());
		}
		else
		{
			super.transferInputToDataElement(rComponent, rElement);
		}
	}

	/***************************************
	 * Updates a {@link Timetable} component from the associated data element.
	 *
	 * @param rTimetable   The timetable to update
	 * @param rDataElement The date data element to create the timetable for
	 */
	@SuppressWarnings("boxing")
	private void updateTimetable(
		Timetable			rTimetable,
		DateListDataElement rDataElement)
	{
		List<DateDataElement> rCalendarEvents = rDataElement.getElements();

		TimetableDisplayStyle eStyle =
			rDataElement.getProperty(TIMETABLE_DISPLAY_STYLE,
									 TimetableDisplayStyle.MONTH);

		rTimetable.setDate(rDataElement.getProperty(DATE, new Date()));
		rTimetable.setTimetableStyle(TimetableStyle.valueOf(eStyle.name()));

		if (eStyle == TimetableDisplayStyle.DAY)
		{
			int nDays = rDataElement.getIntProperty(TIMETABLE_DAYS, 1);

			rTimetable.setVisibleDays(nDays);
		}

		if (rDataElement.hasProperty(TIMETABLE_SCROLL_TO_HOUR))
		{
			rTimetable.scrollToHour(rDataElement.getProperty(TIMETABLE_SCROLL_TO_HOUR,
															 0));
		}

		if (rDataElement.hasProperty(TIMETABLE_SHOW_WEEK_NUMBERS))
		{
			rTimetable.showWeekNumbers(rDataElement.hasFlag(TIMETABLE_SHOW_WEEK_NUMBERS));
		}

		if (rDataElement.hasProperty(TIMETABLE_DAY_START))
		{
			rTimetable.setDayStart(rDataElement.getProperty(TIMETABLE_DAY_START,
															6));
		}

		rTimetable.setHourIntervals(rDataElement.getProperty(TIMETABLE_HOUR_SUBDIVISIONS,
															 -1),
									rDataElement.getProperty(TIMETABLE_HOUR_SUBDIVISION_HEIGHT,
															 -1));
		rTimetable.setWorkingHours(rDataElement.getProperty(TIMETABLE_FIRST_WORKING_HOUR,
															-1),
								   rDataElement.getProperty(TIMETABLE_FIRST_WORKING_HOUR,
															-1));

		rTimetable.clear();
		rTimetable.addEvents(rCalendarEvents);
	}

	//~ Inner Classes ----------------------------------------------------------

	/********************************************************************
	 * A date-specific interaction handler subclass.
	 *
	 * @author eso
	 */
	static class DateListInteractionHandler
		extends DataElementInteractionHandler<DateListDataElement>
	{
		//~ Constructors -------------------------------------------------------

		/***************************************
		 * @see DataElementInteractionHandler#DataElementInteractionHandler(DataElementPanelManager,
		 *      de.esoco.data.element.DataElement)
		 */
		public DateListInteractionHandler(
			DataElementPanelManager rPanelManager,
			DateListDataElement		rDataElement)
		{
			super(rPanelManager, rDataElement);
		}

		//~ Methods ------------------------------------------------------------

		/***************************************
		 * {@inheritDoc}
		 */
		@Override
		public void handleEvent(EWTEvent rEvent)
		{
			Object		    rTarget			 = rEvent.getElement();
			HasProperties   rEditedElement   = null;
			InteractionType eInteractionType = null;

			if (rTarget instanceof HasProperties)
			{
				rEditedElement = (HasProperties) rTarget;
			}

			switch (rEvent.getType())
			{
				case ACTION:

					if (rTarget instanceof Date)
					{
						StringProperties aDateElement = new StringProperties();

						aDateElement.setProperty(StandardProperties.START_DATE,
												 (Date) rTarget);
						eInteractionType = InteractionType.DATE_OPEN;
						rEditedElement   = aDateElement;
					}
					else
					{
						eInteractionType = InteractionType.OPEN;
					}

					break;

				case SELECTION:
					eInteractionType = InteractionType.SELECT;

					break;

				case ELEMENT_CREATED:
					eInteractionType = InteractionType.CREATE;

					break;

				case ELEMENT_UPDATED:
					eInteractionType = InteractionType.UPDATE;

					break;

				case ELEMENT_DELETED:
					eInteractionType = InteractionType.DELETE;

					break;

				default:
			}

			getDataElement().setInteraction(eInteractionType, rEditedElement);

			super.handleEvent(rEvent);
		}

		/***************************************
		 * {@inheritDoc}
		 */
		@Override
		protected Set<EventType> getInteractionEventTypes(
			Component				  aComponent,
			Set<InteractionEventType> rInteractionEventTypes)
		{
			return EnumSet.of(EventType.ACTION,
							  EventType.SELECTION,
							  EventType.ELEMENT_CREATED,
							  EventType.ELEMENT_UPDATED,
							  EventType.ELEMENT_DELETED);
		}
	}
}
