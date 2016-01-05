// Copyright (C) 2015 quelltextlich e.U.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.phabricator.conduit.raw;

import java.util.HashMap;
import java.util.Map;

import org.phabricator.conduit.ConduitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiSessionHandler implements SessionHandler {
	private static final Logger log = LoggerFactory.getLogger(ApiSessionHandler.class);

	private final String apiToken;

	/**
	 * Creates a new instance
	 * <p/>
	 * Before this instance can fill in session data, it needs to have a
	 * {@link ConduitModule} injected through
	 * {@link #setConduitModule(ConduitModule)}.
	 */
	public ApiSessionHandler(String apiToken) {
		this.apiToken = apiToken;
	};

	@Override
	public void fillInSession(final Map<String, Object> params) throws ConduitException {

		final Map<String, Object> conduitParams = new HashMap<String, Object>();
		conduitParams.put("token", apiToken);
		params.put("__conduit__", conduitParams);
	}
}