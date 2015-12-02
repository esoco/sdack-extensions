//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'esoco-gwt-deps' project.
// Copyright 2015 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
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
package de.esoco.gwt.client;

import de.esoco.data.element.DataSetDataElement;
import de.esoco.data.element.DateListDataElement;

import de.esoco.gwt.client.ui.DataElementUI;
import de.esoco.gwt.client.ui.DataElementUIFactory;
import de.esoco.gwt.client.ui.DataElementUIFactory.DataElementUICreator;
import de.esoco.gwt.client.ui.DataSetDataElementUI;
import de.esoco.gwt.client.ui.DateListDataElementUI;


/********************************************************************
 * This class provides the static initialization method {@link #init()} that
 * must be invoked by client code once during application initialization to
 * register the GWT dependencies like the {@link DataElementUI} subclasses
 * defined by this project. This call must occur before the first UI is to be
 * created.
 *
 * @author eso
 */
public class GwtDependencies
{
	//~ Constructors -----------------------------------------------------------

	/***************************************
	 * Private, only static use.
	 */
	private GwtDependencies()
	{
	}

	//~ Static methods ---------------------------------------------------------

	/***************************************
	 * Must be invoked once during application initialization to register the
	 * {@link DataElementUI} subclasses from this project.
	 */
	public static void init()
	{
		DataElementUIFactory.registerDataElementUI(DataSetDataElement.class,
			new DataElementUICreator<DataSetDataElementUI>()
			{
				@Override
				public DataSetDataElementUI create()
				{
					return new DataSetDataElementUI();
				}
			});

		DataElementUIFactory.registerDataElementUI(DateListDataElement.class,
			new DataElementUICreator<DateListDataElementUI>()
			{
				@Override
				public DateListDataElementUI create()
				{
					return new DateListDataElementUI();
				}
			});
	}
}
