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
package de.esoco.gwt.server;

import de.esoco.data.SessionData;
import de.esoco.data.UploadHandler;

import de.esoco.gwt.shared.AuthenticationException;

import de.esoco.lib.logging.Log;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import static de.esoco.gwt.server.AuthenticatedServiceImpl.SESSION_UPLOADS;
import static de.esoco.gwt.server.AuthenticatedServiceImpl.getSessionData;
import static de.esoco.gwt.server.AuthenticatedServiceImpl.setErrorResponse;


/********************************************************************
 * A companion servlet to {@link AuthenticatedServiceImpl} that performs file
 * uploads.
 *
 * @author eso
 */
public final class AuthenticatedUploadServlet extends HttpServlet
{
	//~ Static fields/initializers ---------------------------------------------

	private static final long serialVersionUID = 1L;

	//~ Methods ----------------------------------------------------------------

	/***************************************
	 * Initializes this instance.
	 *
	 * @see HttpServlet#init()
	 */
	@Override
	public void init() throws ServletException
	{
	}

	/***************************************
	 * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPost(
		HttpServletRequest  rRequest,
		HttpServletResponse rResponse) throws ServletException, IOException
	{
		try
		{
			processUploadRequest(rRequest, rResponse);
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}

	/***************************************
	 * Handles an upload POST request.
	 *
	 * @param  rRequest  The POST request to process
	 * @param  rResponse The HTTP response object
	 *
	 * @throws FileUploadException     If the file upload fails
	 * @throws IOException             If an IO operation fails
	 * @throws AuthenticationException If no user is authenticated
	 * @throws Exception               If processing the uploaded data fails
	 */
	private void processUploadRequest(
		HttpServletRequest  rRequest,
		HttpServletResponse rResponse) throws Exception
	{
		String	    sUploadId    = rRequest.getParameter("id");
		SessionData rSessionData = getSessionData(rRequest);

		if (rSessionData != null && sUploadId != null && sUploadId.length() > 0)
		{
			UploadHandler rUploadHandler =
				rSessionData.get(SESSION_UPLOADS).get(sUploadId);

			if (rUploadHandler != null)
			{
				ServletFileUpload aFileUpload = new ServletFileUpload();

				FileItemIterator rIterator =
					aFileUpload.getItemIterator(rRequest);

				while (rIterator.hasNext())
				{
					FileItemStream rItem = rIterator.next();

					if (!rItem.isFormField())
					{
						InputStream rDataStream = rItem.openStream();

						try
						{
							rUploadHandler.processUploadData(rItem.getName(),
															 rItem
															 .getContentType(),
															 rDataStream);
						}
						catch (Exception e)
						{
							Log.warn("Upload failed", e);
							setErrorResponse(rResponse,
											 HttpServletResponse.SC_BAD_REQUEST,
											 e.getMessage());
						}

						rDataStream.close();
					}
				}
			}
			else
			{
				Log.warn("No upload handler for " + sUploadId);
				setErrorResponse(rResponse,
								 HttpServletResponse.SC_BAD_REQUEST,
								 "No upload registered");
			}
		}
		else
		{
			String sMessage =
				rSessionData == null ? "User not authorized" : "Invalid upload";

			setErrorResponse(rResponse,
							 HttpServletResponse.SC_UNAUTHORIZED,
							 sMessage);
		}
	}
}
