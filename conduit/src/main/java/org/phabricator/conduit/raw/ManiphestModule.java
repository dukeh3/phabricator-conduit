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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.phabricator.conduit.ConduitException;
import org.phabricator.conduit.bare.Connection;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Module for Conduit methods starting in 'maniphest.'
 */
public class ManiphestModule extends Module {
	public ManiphestModule(final Connection connection, final SessionHandler sessionHandler) {
		super(connection, sessionHandler);
	}

	public abstract static class AbstractEditTransaction extends HashMap<String, Object> {
	}

	public static class ParentTransaction extends AbstractEditTransaction {

		public ParentTransaction(final String phid) {
			put("type", "parent");
			put("value", phid);
		}
	}

	public static class DescriptionTransaction extends AbstractEditTransaction {
		public DescriptionTransaction(final String description) {
			put("type", "description");
			put("value", description);
		}
	}

	public static class OwnerTransaction extends AbstractEditTransaction {
		public OwnerTransaction(final String owner) {
			put("type", "owner");
			put("value", owner);
		}
	}

	public static class StatusTransaction extends AbstractEditTransaction {
		public StatusTransaction(final String status) {
			put("type", "status");
			put("value", status);
		}
	}

	public static class PriorityTransaction extends AbstractEditTransaction {
		public PriorityTransaction(final int priority) {
			put("type", "priority");
			put("value", priority);
		}
	}

	public static class TitleTransaction extends AbstractEditTransaction {
		public TitleTransaction(final String title) {
			put("type", "title");
			put("value", title);
		}
	}

	public static class ColumnTransaction extends AbstractEditTransaction {
		public ColumnTransaction(final String column) {
			put("type", "column");
			put("value", column);
		}
	}
	
	public static class ViewTransaction extends AbstractEditTransaction {
		public ViewTransaction(final String view) {
			put("type", "view");
			put("value", view);
		}
	}

	public static class SpaceTransaction extends AbstractEditTransaction {
		public SpaceTransaction(final String space) {
			put("type", "space");
			put("value", space);
		}
	}

	public static class EditTransaction extends AbstractEditTransaction {
		public EditTransaction(final String edit) {
			put("type", "edit");
			put("value", edit);
		}
	}

	public static class ProjectsAddTransaction extends AbstractEditTransaction {
		public ProjectsAddTransaction(final List<String> projectsList) {
			put("type", "projects.add");
			put("value", projectsList);
		}
	}

	public static class ProjectsRemoveTransaction extends AbstractEditTransaction {
		public ProjectsRemoveTransaction(final List<String> projectsList) {
			put("type", "projects.remove");
			put("value", projectsList);
		}
	}
	
	public static class ProjectsSetTransaction extends AbstractEditTransaction {
		public ProjectsSetTransaction(final List<String> projectsList) {
			put("type", "projects.set");
			put("value", projectsList);
		}
	}

	public static class SubscribersAddTransaction extends AbstractEditTransaction {
		public SubscribersAddTransaction(final List<String> subscribersList) {
			put("type", "subscribers.add");
			put("value", subscribersList);
		}
	}

	public static class SubscribersRemoveTransaction extends AbstractEditTransaction {
		public SubscribersRemoveTransaction(final List<String> subscribersList) {
			put("type", "subscribers.remove");
			put("value", subscribersList);
		}
	}
	
	public static class SubscribersSetTransaction extends AbstractEditTransaction {
		public SubscribersSetTransaction(final List<String> subscribersList) {
			put("type", "subscribers.set");
			put("value", subscribersList);
		}
	}

	
	public EditTaskResult editTask(List<AbstractEditTransaction> transactions) throws ConduitException {
		return editTask(transactions, null, null);
	}

	public EditTaskResult editTask(List<AbstractEditTransaction> transactions, String id) throws ConduitException {
		return editTask(transactions, "objectIdentifier", id);
	}

	private EditTaskResult editTask(List<AbstractEditTransaction> transactions, String idType, String id)
			throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);

		params.put("transactions", transactions);

		if (idType != null)
			params.put(idType, id);

		final JsonElement callResult = connection.call("maniphest.edit", params);
		final EditTaskResult result = gson.fromJson(callResult, EditTaskResult.class);
		return result;
	}

	public static class ObjectId {
		public String phid;
	}
	
	public static class EditTaskResult {
		public ObjectId object;
	}

	/**
	 * Runs the API's 'maniphest.createtask' method
	 */
	public CreateTaskResult createTask(final String title, final String description, final String ownerPhid,
			final String viewPolicy, final String editPolicy, final List<String> ccPhids, final Integer priority,
			final List<String> projectPhids, final Map<String, String> auxiliary) throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);
		params.put("title", title);
		params.put("description", description);
		params.put("ownerPHID", ownerPhid);
		params.put("viewPolicy", viewPolicy);
		params.put("editPolicy", editPolicy);
		params.put("ccPHIDs", ccPhids);
		params.put("priority", priority);
		params.put("projectPHIDs", projectPhids);
		params.put("auxiliary", auxiliary);

		final JsonElement callResult = connection.call("maniphest.createtask", params);
		final CreateTaskResult result = gson.fromJson(callResult, CreateTaskResult.class);
		return result;
	}

	/**
	 * Models the result for a call to 'maniphest.createtask'
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "id":"48",
	 *   "phid":"PHID-TASK-pemd324eosnymq3tdkyo",
	 *   "authorPHID":"PHID-USER-na3one2sht11aone",
	 *   "ownerPHID":null,
	 *   "ccPHIDs":[
	 *     "PHID-USER-h4n62fq2kt2v3a2qjyqh"
	 *   ],
	 *   "status":"open",
	 *   "statusName":"Open",
	 *   "isClosed":false,
	 *   "priority": "Needs Triage",
	 *   "priorityColor":"violet",
	 *   "title":"QChris test task",
	 *   "description":"",
	 *   "projectPHIDs":[],
	 *   "uri":"https://phabricator.local/T47",
	 *   "auxiliary":{
	 *     "std:maniphest:security_topic":"default",
	 *     "isdc:sprint:storypoints":null
	 *   },
	 *   "objectName":"T47",
	 *   "dateCreated":"1413484594",
	 *   "dateModified":1413549869,
	 *   "dependsOnTaskPHIDs":[]
	 * }
	 * </pre>
	 */
	public static class CreateTaskResult extends TaskResult {
		public CreateTaskResult(final int id, final String phid, final String authorPHID, final String ownerPHID,
				final List<String> ccPHIDs, final String status, final String statusName, final Boolean isClosed,
				final String priority, final String priorityColor, final String title, final String description,
				final List<String> projectPHIDs, final String uri, final Map<String, String> auxiliary,
				final String objectName, final String dateCreated, final String dateModified,
				final List<String> dependsOnTaskPHIDs) {
			super(id, phid, authorPHID, ownerPHID, ccPHIDs, status, statusName, isClosed, priority, priorityColor,
					title, description, projectPHIDs, uri, auxiliary, objectName, dateCreated, dateModified,
					dependsOnTaskPHIDs);
		}
	}

	/**
	 * Runs the API's 'maniphest.getTaskTransactions' method
	 */
	public GetTaskTransactionsResult getTaskTransactions(final List<Integer> ids) throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);
		params.put("ids", ids);

		final JsonElement callResult = connection.call("maniphest.gettasktransactions", params);
		final GetTaskTransactionsResult result = gson.fromJson(callResult, GetTaskTransactionsResult.class);
		return result;
	}

	/**
	 * Models the result for a call to 'maniphest.gettasktransactions'
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "85": [
	 *     {
	 *       "taskID": "85",
	 *       "transactionPHID": "PHID-XACT-TASK-o3ofscpf5cs3wj3",
	 *       "transactionType": "core:comment",
	 *       "oldValue": null,
	 *       "newValue": null,
	 *       "comments": "Test comment",
	 *       "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *       "dateCreated": "1436473965"
	 *     },
	 *     {
	 *       "taskID": "85",
	 *       "transactionPHID": "PHID-XACT-TASK-7o4g3dpn6izhslq",
	 *       "transactionType": "priority",
	 *       "oldValue": 25,
	 *       "newValue": 80,
	 *       "comments": null,
	 *       "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *       "dateCreated": "1436473961"
	 *     }
	 *   ],
	 *   "86": [
	 *     {
	 *       "taskID": "86",
	 *       "transactionPHID": "PHID-XACT-TASK-xhbr4gj7ca224b2",
	 *       "transactionType": "priority",
	 *       "oldValue": null,
	 *       "newValue": 25,
	 *       "comments": null,
	 *       "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *       "dateCreated": "1436473263"
	 *     },
	 *     {
	 *       "taskID": "86",
	 *       "transactionPHID": "PHID-XACT-TASK-ghyh4ue3p4m3yqz",
	 *       "transactionType": "core:subscribers",
	 *       "oldValue": [],
	 *       "newValue": [
	 *         "PHID-USER-3nphm6xkw2mpyfshq4dq"
	 *       ],
	 *       "comments": null,
	 *       "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *       "dateCreated": "1436473263"
	 *     },
	 *     {
	 *       "taskID": "86",
	 *       "transactionPHID": "PHID-XACT-TASK-icdkynz2de3wuf2",
	 *       "transactionType": "status",
	 *       "oldValue": null,
	 *       "newValue": "open",
	 *       "comments": null,
	 *       "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *       "dateCreated": "1436473263"
	 *     }
	 *   ],
	 * }
	 * </pre>
	 */
	public static class GetTaskTransactionsResult extends HashMap<String, SingleGetTaskTransactionsResult> {
		private static final long serialVersionUID = 1L;
	}

	public static class SingleGetTaskTransactionsResult extends ArrayList<TaskTransaction> {
		private static final long serialVersionUID = 1L;
	}

	public static class TaskTransaction {
		@SerializedName("taskID")
		private final String taskId;
		@SerializedName("transactionPHID")
		private final String transactionPhid;
		private final String transactionType;
		private final Object oldValue;
		private final Object newValue;
		private final String comments;
		@SerializedName("authorPHID")
		private final String authorPhid;
		private final String dateCreated;

		public TaskTransaction(final String taskId, final String transactionPhid, final String transactionType,
				final Object oldValue, final Object newValue, final String comments, final String authorPhid,
				final String dateCreated) {
			super();
			this.taskId = taskId;
			this.transactionPhid = transactionPhid;
			this.transactionType = transactionType;
			this.oldValue = oldValue;
			this.newValue = newValue;
			this.comments = comments;
			this.authorPhid = authorPhid;
			this.dateCreated = dateCreated;
		}

		public String getTaskId() {
			return taskId;
		}

		public String getTransactionPhid() {
			return transactionPhid;
		}

		public String getTransactionType() {
			return transactionType;
		}

		public Object getOldValue() {
			return oldValue;
		}

		public Object getNewValue() {
			return newValue;
		}

		public String getComments() {
			return comments;
		}

		public String getAuthorPhid() {
			return authorPhid;
		}

		public String getDateCreated() {
			return dateCreated;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((authorPhid == null) ? 0 : authorPhid.hashCode());
			result = prime * result + ((comments == null) ? 0 : comments.hashCode());
			result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
			result = prime * result + ((newValue == null) ? 0 : newValue.hashCode());
			result = prime * result + ((oldValue == null) ? 0 : oldValue.hashCode());
			result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
			result = prime * result + ((transactionPhid == null) ? 0 : transactionPhid.hashCode());
			result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final TaskTransaction other = (TaskTransaction) obj;
			if (authorPhid == null) {
				if (other.authorPhid != null) {
					return false;
				}
			} else if (!authorPhid.equals(other.authorPhid)) {
				return false;
			}
			if (comments == null) {
				if (other.comments != null) {
					return false;
				}
			} else if (!comments.equals(other.comments)) {
				return false;
			}
			if (dateCreated == null) {
				if (other.dateCreated != null) {
					return false;
				}
			} else if (!dateCreated.equals(other.dateCreated)) {
				return false;
			}
			if (newValue == null) {
				if (other.newValue != null) {
					return false;
				}
			} else if (!newValue.equals(other.newValue)) {
				return false;
			}
			if (oldValue == null) {
				if (other.oldValue != null) {
					return false;
				}
			} else if (!oldValue.equals(other.oldValue)) {
				return false;
			}
			if (taskId == null) {
				if (other.taskId != null) {
					return false;
				}
			} else if (!taskId.equals(other.taskId)) {
				return false;
			}
			if (transactionPhid == null) {
				if (other.transactionPhid != null) {
					return false;
				}
			} else if (!transactionPhid.equals(other.transactionPhid)) {
				return false;
			}
			if (transactionType == null) {
				if (other.transactionType != null) {
					return false;
				}
			} else if (!transactionType.equals(other.transactionType)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "TaskTransactionResult [taskId=" + taskId + ", transactionPhid=" + transactionPhid
					+ ", transactionType=" + transactionType + ", oldValue=" + oldValue + ", newValue=" + newValue
					+ ", comments=" + comments + ", authorPhid=" + authorPhid + ", dateCreated=" + dateCreated + "]";
		}
	}

	/**
	 * Runs the API's 'maniphest.Info' method
	 */
	public InfoResult info(final int taskId) throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);
		params.put("task_id", taskId);

		final JsonElement callResult = connection.call("maniphest.info", params);
		final InfoResult result = gson.fromJson(callResult, InfoResult.class);
		return result;
	}

	/**
	 * Models the result for a call to maniphest.info
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "id":"48",
	 *   "phid":"PHID-TASK-pemd324eosnymq3tdkyo",
	 *   "authorPHID":"PHID-USER-na3one2sht11aone",
	 *   "ownerPHID":null,
	 *   "ccPHIDs":[
	 *     "PHID-USER-h4n62fq2kt2v3a2qjyqh"
	 *   ],
	 *   "status":"open",
	 *   "statusName":"Open",
	 *   "isClosed":false,
	 *   "priority": "Needs Triage",
	 *   "priorityColor":"violet",
	 *   "title":"QChris test task",
	 *   "description":"",
	 *   "projectPHIDs":[],
	 *   "uri":"https://phabricator.local/T47",
	 *   "auxiliary":{
	 *     "std:maniphest:security_topic":"default",
	 *     "isdc:sprint:storypoints":null
	 *   },
	 *   "objectName":"T47",
	 *   "dateCreated":"1413484594",
	 *   "dateModified":1413549869,
	 *   "dependsOnTaskPHIDs":[]
	 * }
	 * </pre>
	 */
	public static class InfoResult extends TaskResult {
		public InfoResult(final int id, final String phid, final String authorPHID, final String ownerPHID,
				final List<String> ccPHIDs, final String status, final String statusName, final Boolean isClosed,
				final String priority, final String priorityColor, final String title, final String description,
				final List<String> projectPHIDs, final String uri, final Map<String, String> auxiliary,
				final String objectName, final String dateCreated, final String dateModified,
				final List<String> dependsOnTaskPHIDs) {
			super(id, phid, authorPHID, ownerPHID, ccPHIDs, status, statusName, isClosed, priority, priorityColor,
					title, description, projectPHIDs, uri, auxiliary, objectName, dateCreated, dateModified,
					dependsOnTaskPHIDs);
		}
	}

	/**
	 * Runs the API's 'maniphest.update' method
	 */
	public UpdateResult update(final Integer id, final String phid, final String title, final String description,
			final String ownerPhid, final String viewPolicy, final String editPolicy, final List<String> ccPhids,
			final Integer priority, final List<String> projectPhids, final Map<String, String> auxiliary,
			final String status, final String comments) throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);
		params.put("id", id);
		params.put("phid", phid);
		params.put("title", title);
		params.put("description", description);
		params.put("ownerPHID", ownerPhid);
		params.put("viewPolicy", viewPolicy);
		params.put("editPolicy", editPolicy);
		params.put("ccPHIDs", ccPhids);
		params.put("priority", priority);
		params.put("projectPHIDs", projectPhids);
		params.put("auxiliary", auxiliary);
		params.put("status", status);
		params.put("comments", comments);

		final JsonElement callResult = connection.call("maniphest.update", params);
		final UpdateResult result = gson.fromJson(callResult, UpdateResult.class);
		return result;
	}

	/**
	 * Models the result for a call to maniphest.update
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "id":"48",
	 *   "phid":"PHID-TASK-pemd324eosnymq3tdkyo",
	 *   "authorPHID":"PHID-USER-na3one2sht11aone",
	 *   "ownerPHID":null,
	 *   "ccPHIDs":[
	 *     "PHID-USER-h4n62fq2kt2v3a2qjyqh"
	 *   ],
	 *   "status":"open",
	 *   "statusName":"Open",
	 *   "isClosed":false,
	 *   "priority": "Needs Triage",
	 *   "priorityColor":"violet",
	 *   "title":"QChris test task",
	 *   "description":"",
	 *   "projectPHIDs":[],
	 *   "uri":"https://phabricator.local/T47",
	 *   "auxiliary":{
	 *     "std:maniphest:security_topic":"default",
	 *     "isdc:sprint:storypoints":null
	 *   },
	 *   "objectName":"T47",
	 *   "dateCreated":"1413484594",
	 *   "dateModified":1413549869,
	 *   "dependsOnTaskPHIDs":[]
	 * }
	 * </pre>
	 */
	public static class UpdateResult extends TaskResult {
		public UpdateResult(final int id, final String phid, final String authorPHID, final String ownerPHID,
				final List<String> ccPHIDs, final String status, final String statusName, final Boolean isClosed,
				final String priority, final String priorityColor, final String title, final String description,
				final List<String> projectPHIDs, final String uri, final Map<String, String> auxiliary,
				final String objectName, final String dateCreated, final String dateModified,
				final List<String> dependsOnTaskPHIDs) {
			super(id, phid, authorPHID, ownerPHID, ccPHIDs, status, statusName, isClosed, priority, priorityColor,
					title, description, projectPHIDs, uri, auxiliary, objectName, dateCreated, dateModified,
					dependsOnTaskPHIDs);
		}
	}

	/**
	 * Models the result for API methods returning TaskResult information
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "id":"48",
	 *   "phid":"PHID-TASK-pemd324eosnymq3tdkyo",
	 *   "authorPHID":"PHID-USER-na3one2sht11aone",
	 *   "ownerPHID":null,
	 *   "ccPHIDs":[
	 *     "PHID-USER-h4n62fq2kt2v3a2qjyqh"
	 *   ],
	 *   "status":"open",
	 *   "statusName":"Open",
	 *   "isClosed":false,
	 *   "priority": "Needs Triage",
	 *   "priorityColor":"violet",
	 *   "title":"QChris test task",
	 *   "description":"",
	 *   "projectPHIDs":[],
	 *   "uri":"https://phabricator.local/T47",
	 *   "auxiliary":{
	 *     "std:maniphest:security_topic":"default",
	 *     "isdc:sprint:storypoints":null
	 *   },
	 *   "objectName":"T47",
	 *   "dateCreated":"1413484594",
	 *   "dateModified":1413549869,
	 *   "dependsOnTaskPHIDs":[]
	 * }
	 * </pre>
	 */
	public static class TaskResult {
		private final int id;
		private final String phid;
		private final String authorPHID;
		private final String ownerPHID;
		private final List<String> ccPHIDs;
		private final String status;
		private final String statusName;
		private final Boolean isClosed;
		private final String priority;
		private final String priorityColor;
		private final String title;
		private final String description;
		private final List<String> projectPHIDs;
		private final String uri;
		private final Map<String, String> auxiliary;
		private final String objectName;
		private final String dateCreated;
		private final String dateModified;
		private final List<String> dependsOnTaskPHIDs;

		public TaskResult(final int id, final String phid, final String authorPHID, final String ownerPHID,
				final List<String> ccPHIDs, final String status, final String statusName, final Boolean isClosed,
				final String priority, final String priorityColor, final String title, final String description,
				final List<String> projectPHIDs, final String uri, final Map<String, String> auxiliary,
				final String objectName, final String dateCreated, final String dateModified,
				final List<String> dependsOnTaskPHIDs) {
			super();
			this.id = id;
			this.phid = phid;
			this.authorPHID = authorPHID;
			this.ownerPHID = ownerPHID;
			this.ccPHIDs = ccPHIDs;
			this.status = status;
			this.statusName = statusName;
			this.isClosed = isClosed;
			this.priority = priority;
			this.priorityColor = priorityColor;
			this.title = title;
			this.description = description;
			this.projectPHIDs = projectPHIDs;
			this.uri = uri;
			this.auxiliary = auxiliary;
			this.objectName = objectName;
			this.dateCreated = dateCreated;
			this.dateModified = dateModified;
			this.dependsOnTaskPHIDs = dependsOnTaskPHIDs;
		}

		public int getId() {
			return id;
		}

		public String getPhid() {
			return phid;
		}

		public String getAuthorPHID() {
			return authorPHID;
		}

		public String getOwnerPHID() {
			return ownerPHID;
		}

		public List<String> getCcPHIDs() {
			return ccPHIDs;
		}

		public String getStatus() {
			return status;
		}

		public String getStatusName() {
			return statusName;
		}

		public Boolean getIsClosed() {
			return isClosed;
		}

		public String getPriority() {
			return priority;
		}

		public String getPriorityColor() {
			return priorityColor;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public List<String> getProjectPHIDs() {
			return projectPHIDs;
		}

		public String getUri() {
			return uri;
		}

		public Map<String, String> getAuxiliary() {
			return auxiliary;
		}

		public String getObjectName() {
			return objectName;
		}

		public String getDateCreated() {
			return dateCreated;
		}

		public String getDateModified() {
			return dateModified;
		}

		public List<String> getDependsOnTaskPHIDs() {
			return dependsOnTaskPHIDs;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((authorPHID == null) ? 0 : authorPHID.hashCode());
			result = prime * result + ((auxiliary == null) ? 0 : auxiliary.hashCode());
			result = prime * result + ((ccPHIDs == null) ? 0 : ccPHIDs.hashCode());
			result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
			result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
			result = prime * result + ((dependsOnTaskPHIDs == null) ? 0 : dependsOnTaskPHIDs.hashCode());
			result = prime * result + ((description == null) ? 0 : description.hashCode());
			result = prime * result + id;
			result = prime * result + ((isClosed == null) ? 0 : isClosed.hashCode());
			result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
			result = prime * result + ((ownerPHID == null) ? 0 : ownerPHID.hashCode());
			result = prime * result + ((phid == null) ? 0 : phid.hashCode());
			result = prime * result + ((priority == null) ? 0 : priority.hashCode());
			result = prime * result + ((priorityColor == null) ? 0 : priorityColor.hashCode());
			result = prime * result + ((projectPHIDs == null) ? 0 : projectPHIDs.hashCode());
			result = prime * result + ((status == null) ? 0 : status.hashCode());
			result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			result = prime * result + ((uri == null) ? 0 : uri.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final TaskResult other = (TaskResult) obj;
			if (authorPHID == null) {
				if (other.authorPHID != null) {
					return false;
				}
			} else if (!authorPHID.equals(other.authorPHID)) {
				return false;
			}
			if (auxiliary == null) {
				if (other.auxiliary != null) {
					return false;
				}
			} else if (!auxiliary.equals(other.auxiliary)) {
				return false;
			}
			if (ccPHIDs == null) {
				if (other.ccPHIDs != null) {
					return false;
				}
			} else if (!ccPHIDs.equals(other.ccPHIDs)) {
				return false;
			}
			if (dateCreated == null) {
				if (other.dateCreated != null) {
					return false;
				}
			} else if (!dateCreated.equals(other.dateCreated)) {
				return false;
			}
			if (dateModified == null) {
				if (other.dateModified != null) {
					return false;
				}
			} else if (!dateModified.equals(other.dateModified)) {
				return false;
			}
			if (dependsOnTaskPHIDs == null) {
				if (other.dependsOnTaskPHIDs != null) {
					return false;
				}
			} else if (!dependsOnTaskPHIDs.equals(other.dependsOnTaskPHIDs)) {
				return false;
			}
			if (description == null) {
				if (other.description != null) {
					return false;
				}
			} else if (!description.equals(other.description)) {
				return false;
			}
			if (id != other.id) {
				return false;
			}
			if (isClosed == null) {
				if (other.isClosed != null) {
					return false;
				}
			} else if (!isClosed.equals(other.isClosed)) {
				return false;
			}
			if (objectName == null) {
				if (other.objectName != null) {
					return false;
				}
			} else if (!objectName.equals(other.objectName)) {
				return false;
			}
			if (ownerPHID == null) {
				if (other.ownerPHID != null) {
					return false;
				}
			} else if (!ownerPHID.equals(other.ownerPHID)) {
				return false;
			}
			if (phid == null) {
				if (other.phid != null) {
					return false;
				}
			} else if (!phid.equals(other.phid)) {
				return false;
			}
			if (priority == null) {
				if (other.priority != null) {
					return false;
				}
			} else if (!priority.equals(other.priority)) {
				return false;
			}
			if (priorityColor == null) {
				if (other.priorityColor != null) {
					return false;
				}
			} else if (!priorityColor.equals(other.priorityColor)) {
				return false;
			}
			if (projectPHIDs == null) {
				if (other.projectPHIDs != null) {
					return false;
				}
			} else if (!projectPHIDs.equals(other.projectPHIDs)) {
				return false;
			}
			if (status == null) {
				if (other.status != null) {
					return false;
				}
			} else if (!status.equals(other.status)) {
				return false;
			}
			if (statusName == null) {
				if (other.statusName != null) {
					return false;
				}
			} else if (!statusName.equals(other.statusName)) {
				return false;
			}
			if (title == null) {
				if (other.title != null) {
					return false;
				}
			} else if (!title.equals(other.title)) {
				return false;
			}
			if (uri == null) {
				if (other.uri != null) {
					return false;
				}
			} else if (!uri.equals(other.uri)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "TaskResult [id=" + id + ", phid=" + phid + ", authorPHID=" + authorPHID + ", ownerPHID=" + ownerPHID
					+ ", ccPHIDs=" + ccPHIDs + ", status=" + status + ", statusName=" + statusName + ", isClosed="
					+ isClosed + ", priority=" + priority + ", priorityColor=" + priorityColor + ", title=" + title
					+ ", description=" + description + ", projectPHIDs=" + projectPHIDs + ", uri=" + uri
					+ ", auxiliary=" + auxiliary + ", objectName=" + objectName + ", dateCreated=" + dateCreated
					+ ", dateModified=" + dateModified + ", dependsOnTaskPHIDs=" + dependsOnTaskPHIDs + "]";
		}
	}

	/**
	 * Runs the API's 'maniphest.query' method
	 */
	public QueryResult query(final List<Integer> ids, final List<String> phids, final List<String> ownerPhids,
			final List<String> authorPhids, final List<String> projectPhids, final List<String> ccPhids,
			final String fullText, final String status, final String order, final Integer limit, final Integer offset)
			throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);
		params.put("ids", ids);
		params.put("phids", phids);
		params.put("ownerPHIDs", ownerPhids);
		params.put("authorPHIDs", authorPhids);
		params.put("projectPHIDs", projectPhids);
		params.put("ccPHIDs", ccPhids);
		params.put("fullText", fullText);
		params.put("status", status);
		params.put("order", order);
		params.put("limit", limit);
		params.put("offset", offset);

		final JsonElement callResult = connection.call("maniphest.query", params);
		final QueryResult result = gson.fromJson(callResult, QueryResult.class);
		return result;
	}

	/**
	 * Models the result for a call to maniphest.query
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "PHID-TASK-mxb6ywkxbd5mhjjsitf4": {
	 *     "id": "89",
	 *     "phid": "PHID-TASK-mxb6ywkxbd5mhjjsitf4",
	 *     "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *     "ownerPHID": null,
	 *     "ccPHIDs": [
	 *       "PHID-USER-3nphm6xkw2mpyfshq4dq"
	 *     ],
	 *     "status": "open",
	 *     "statusName": "Open",
	 *     "isClosed": false,
	 *     "priority": "Low",
	 *     "priorityColor": "yellow",
	 *     "title": "qchris-test-task2",
	 *     "description": "description",
	 *     "projectPHIDs": [],
	 *     "uri": "https:\/\/phabricator.local\/T89",
	 *     "auxiliary": {
	 *       "std:maniphest:security_topic": null,
	 *       "isdc:sprint:storypoints": null
	 *     },
	 *     "objectName": "T89",
	 *     "dateCreated": "1436474174",
	 *     "dateModified": "1436474174",
	 *     "dependsOnTaskPHIDs": []
	 *   },
	 *   "PHID-TASK-k4d7asi554br34urlbt2": {
	 *     "id": "88",
	 *     "phid": "PHID-TASK-k4d7asi554br34urlbt2",
	 *     "authorPHID": "PHID-USER-3nphm6xkw2mpyfshq4dq",
	 *     "ownerPHID": null,
	 *     "ccPHIDs": [
	 *       "PHID-USER-3nphm6xkw2mpyfshq4dq"
	 *     ],
	 *     "status": "open",
	 *     "statusName": "Open",
	 *     "isClosed": false,
	 *     "priority": "Low",
	 *     "priorityColor": "yellow",
	 *     "title": "qchris-test-task2",
	 *     "description": "description",
	 *     "projectPHIDs": [],
	 *     "uri": "https:\/\/phabricator.local\/T88",
	 *     "auxiliary": {
	 *       "std:maniphest:security_topic": null,
	 *       "isdc:sprint:storypoints": null
	 *     },
	 *     "objectName": "T88",
	 *     "dateCreated": "1436473926",
	 *     "dateModified": "1436473926",
	 *     "dependsOnTaskPHIDs": []
	 *   },
	 *   [...]
	 * }
	 * </pre>
	 */
	public static class QueryResult extends HashMap<String, TaskResult> {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Runs the API's 'maniphest.querystatuses' method
	 */
	public QueryStatusesResult queryStatuses() throws ConduitException {
		final Map<String, Object> params = new HashMap<String, Object>();
		sessionHandler.fillInSession(params);

		final JsonElement callResult = connection.call("maniphest.querystatuses", params);
		final QueryStatusesResult result = gson.fromJson(callResult, QueryStatusesResult.class);
		return result;
	}

	/**
	 * Models the result for a call to maniphest.querystatuses
	 * <p/>
	 * JSON looks like:
	 *
	 * <pre>
	 * {
	 *   "defaultStatus": "open",
	 *   "defaultClosedStatus": "resolved",
	 *   "duplicateStatus": "duplicate",
	 *   "openStatuses": [
	 *     "open",
	 *     "stalled"
	 *   ],
	 *   "closedStatuses": {
	 *     "1": "resolved",
	 *     "3": "declined",
	 *     "4": "duplicate",
	 *     "5": "invalid"
	 *   },
	 *   "allStatuses": [
	 *     "open",
	 *     "resolved",
	 *     "stalled",
	 *     "declined",
	 *     "duplicate",
	 *     "invalid"
	 *   ],
	 *   "statusMap": {
	 *     "open": "Open",
	 *     "resolved": "Resolved",
	 *     "stalled": "Stalled",
	 *     "declined": "Declined",
	 *     "duplicate": "Duplicate",
	 *     "invalid": "Invalid"
	 *   }
	 * }
	 * </pre>
	 */
	public static class QueryStatusesResult {
		private final String defaultStatus;
		private final String defaultClosedStatus;
		private final String duplicateStatus;
		private final List<String> openStatuses;
		private final Map<String, String> closedStatuses;
		private final List<String> allStatuses;
		private final Map<String, String> statusMap;

		public QueryStatusesResult(final String defaultStatus, final String defaultClosedStatus,
				final String duplicateStatus, final List<String> openStatuses, final Map<String, String> closedStatuses,
				final List<String> allStatuses, final Map<String, String> statusMap) {
			super();
			this.defaultStatus = defaultStatus;
			this.defaultClosedStatus = defaultClosedStatus;
			this.duplicateStatus = duplicateStatus;
			this.openStatuses = openStatuses;
			this.closedStatuses = closedStatuses;
			this.allStatuses = allStatuses;
			this.statusMap = statusMap;
		}

		public String getDefaultStatus() {
			return defaultStatus;
		}

		public String getDefaultClosedStatus() {
			return defaultClosedStatus;
		}

		public String getDuplicateStatus() {
			return duplicateStatus;
		}

		public List<String> getOpenStatuses() {
			return openStatuses;
		}

		public Map<String, String> getClosedStatuses() {
			return closedStatuses;
		}

		public List<String> getAllStatuses() {
			return allStatuses;
		}

		public Map<String, String> getStatusMap() {
			return statusMap;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((allStatuses == null) ? 0 : allStatuses.hashCode());
			result = prime * result + ((closedStatuses == null) ? 0 : closedStatuses.hashCode());
			result = prime * result + ((defaultClosedStatus == null) ? 0 : defaultClosedStatus.hashCode());
			result = prime * result + ((defaultStatus == null) ? 0 : defaultStatus.hashCode());
			result = prime * result + ((duplicateStatus == null) ? 0 : duplicateStatus.hashCode());
			result = prime * result + ((openStatuses == null) ? 0 : openStatuses.hashCode());
			result = prime * result + ((statusMap == null) ? 0 : statusMap.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final QueryStatusesResult other = (QueryStatusesResult) obj;
			if (allStatuses == null) {
				if (other.allStatuses != null) {
					return false;
				}
			} else if (!allStatuses.equals(other.allStatuses)) {
				return false;
			}
			if (closedStatuses == null) {
				if (other.closedStatuses != null) {
					return false;
				}
			} else if (!closedStatuses.equals(other.closedStatuses)) {
				return false;
			}
			if (defaultClosedStatus == null) {
				if (other.defaultClosedStatus != null) {
					return false;
				}
			} else if (!defaultClosedStatus.equals(other.defaultClosedStatus)) {
				return false;
			}
			if (defaultStatus == null) {
				if (other.defaultStatus != null) {
					return false;
				}
			} else if (!defaultStatus.equals(other.defaultStatus)) {
				return false;
			}
			if (duplicateStatus == null) {
				if (other.duplicateStatus != null) {
					return false;
				}
			} else if (!duplicateStatus.equals(other.duplicateStatus)) {
				return false;
			}
			if (openStatuses == null) {
				if (other.openStatuses != null) {
					return false;
				}
			} else if (!openStatuses.equals(other.openStatuses)) {
				return false;
			}
			if (statusMap == null) {
				if (other.statusMap != null) {
					return false;
				}
			} else if (!statusMap.equals(other.statusMap)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "QueryStatusesResult [defaultStatus=" + defaultStatus + ", defaultClosedStatus="
					+ defaultClosedStatus + ", duplicateStatus=" + duplicateStatus + ", openStatuses=" + openStatuses
					+ ", closedStatuses=" + closedStatuses + ", allStatuses=" + allStatuses + ", statusMap=" + statusMap
					+ "]";
		}
	}
}
