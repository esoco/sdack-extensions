//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'sdack-extensions' project.
// Copyright 2016 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
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

import de.esoco.data.element.DataSetDataElement;
import de.esoco.data.element.DataSetDataElement.LegendPosition;

import de.esoco.ewt.build.ChartBuilder;
import de.esoco.ewt.build.ContainerBuilder;
import de.esoco.ewt.component.Chart;
import de.esoco.ewt.component.Chart.ChartLegendPosition;
import de.esoco.ewt.component.Chart.ChartType;
import de.esoco.ewt.component.Component;
import de.esoco.ewt.graphics.Color;
import de.esoco.ewt.style.StyleData;

import de.esoco.lib.model.DataSet;
import de.esoco.lib.property.StyleProperties;

import static de.esoco.data.element.DataSetDataElement.CHART_3D;
import static de.esoco.data.element.DataSetDataElement.CHART_BACKGROUND;
import static de.esoco.data.element.DataSetDataElement.CHART_LEGEND_POSITION;
import static de.esoco.data.element.DataSetDataElement.CHART_TYPE;


/********************************************************************
 * The user interface implementation for boolean data elements.
 *
 * @author eso
 */
public class DataSetDataElementUI extends DataElementUI<DataSetDataElement>
{
	//~ Constructors -----------------------------------------------------------

	/***************************************
	 * {@inheritDoc}
	 */
	public DataSetDataElementUI()
	{
	}

	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected Component createDisplayUI(ContainerBuilder<?> rBuilder,
										StyleData			rDisplayStyle,
										DataSetDataElement  rDataElement)
	{
		Chart aChart = ChartBuilder.addChart(rBuilder, rDisplayStyle, null);

		setChartParameters(aChart, rDataElement);

		return aChart;
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected Component createInputUI(ContainerBuilder<?> rBuilder,
									  StyleData			  rDisplayStyle,
									  DataSetDataElement  rDataElement)
	{
		Chart aChart = ChartBuilder.addChart(rBuilder, rDisplayStyle, null);

		setChartParameters(aChart, rDataElement);

		return aChart;
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected void transferDataElementValueToComponent(
		DataSetDataElement rDataElement,
		Component		   rComponent)
	{
		setChartParameters((Chart) rComponent, rDataElement);
	}

	/***************************************
	 * {@inheritDoc}
	 */
	@Override
	protected void transferInputToDataElement(
		Component		   rComponent,
		DataSetDataElement rDataElement)
	{
		// can be ignored because the chart data is not modified by the client
	}

	/***************************************
	 * Sets the parameters of the chart component from the data element.
	 *
	 * @param aChart       The chart component
	 * @param rDataElement The data element
	 */
	private void setChartParameters(
		Chart			   aChart,
		DataSetDataElement rDataElement)
	{
		DataSet<?> rDataSet = rDataElement.getValue();

		String  sBackgroundColor;
		Integer rBackgroundColor =
			rDataElement.getProperty(StyleProperties.BACKGROUND_COLOR, null);

		if (rBackgroundColor != null)
		{
			sBackgroundColor = Color.toHtml(rBackgroundColor.intValue());
		}
		else
		{
			sBackgroundColor = rDataElement.getProperty(CHART_BACKGROUND, null);
		}

		ChartType eChartType =
			ChartType.valueOf(rDataElement.getProperty(CHART_TYPE,
													   DataSetDataElement
													   .ChartType.PIE).name());

		LegendPosition eLegendPosition =
			rDataElement.getProperty(CHART_LEGEND_POSITION,
									 LegendPosition.NONE);

		ChartLegendPosition eChartLegendPosition =
			ChartLegendPosition.valueOf(eLegendPosition.name());

		aChart.setChartType(eChartType);
		aChart.setBackgroundColor(sBackgroundColor);
		aChart.setLegendPosition(eChartLegendPosition);
		aChart.set3D(rDataElement.hasFlag(CHART_3D));
		aChart.setData(rDataSet);
		aChart.repaint();
	}
}
