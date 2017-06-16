package se.h3.ph.tt;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.phabricator.conduit.ConduitException;
import org.phabricator.conduit.raw.Conduit;
import org.phabricator.conduit.raw.ManiphestModule.TaskResult;
import org.phabricator.conduit.raw.ManiphestModule.TaskTransaction;
import org.phabricator.conduit.raw.ProjectModule.ProjectResult;
import org.phabricator.conduit.raw.UserModule.UserResult;

public class ConduitWrapper {

	public Conduit conduit;

	public UserResult getUserByPhid(String phid) {
		List<UserResult> qr = getUsersByPhids(Arrays.asList(phid));
		return qr.isEmpty() ? null : qr.iterator().next();
	}

	public List<UserResult> getAllUsers() {
		return getUsersByPhids(null);
	}

	public List<UserResult> getUsersByPhids(List<String> phids) {
		try {
			return conduit.getUserModule().query(null, null, null, phids, null, null, null);
		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public UserResult getUserById(String id) {
		List<UserResult> qr = getUsersByIds(Arrays.asList(id));
		return qr.isEmpty() ? null : qr.iterator().next();
	}

	public List<UserResult> getUsersByIds(List<String> ids) {
		try {
			return conduit.getUserModule().query(ids, null, null, null, null, null, null);

		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<TaskResult> getAllTasks() {
		return getTasksForProjects(null);
	}

	public TaskResult getTaskByPhid(String phid) {
		return getTasksByPhids(Arrays.asList(phid)).iterator().next();
	}

	public Collection<TaskResult> getTasksByPhids(List<String> phids) {
		try {
			return conduit.maniphest.query(null, phids, null, null, null, null, null, null, null, null, null).values();

		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<TaskResult> getTasksForProject(String phid) {
		return getTasksForProjects(Arrays.asList(phid));
	}

	public Collection<TaskResult> getTasksForProjects(List<String> phids) {
		try {
			return conduit.maniphest.query(null, null, null, null, phids, null, null, null, null, null, null).values();

		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<TaskResult> getTasksForUser(String phid) {
		return getTasksForUsers(Arrays.asList(phid));
	}

	public Collection<TaskResult> getTasksForUsers(List<String> phids) {
		try {
			return conduit.maniphest.query(null, null, phids, null, null, null, null, null, null, null, null).values();

		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}


	public List<TaskTransaction> getTransactionsForTask(int ti) { 
		 return getTransactionsForTaskList(Arrays.asList(new Integer[] { ti })).get(ti);
	}
	
	public Map<String, ? extends List<TaskTransaction>> getTransactionsForTaskList(List<Integer> til) {
		try {
			return conduit.maniphest.getTaskTransactions(til);
		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	public ProjectResult getProjectByName(String name) {
		Collection<ProjectResult> qr = getProjectsByNames(Arrays.asList(name));
		return qr.isEmpty() ? null : qr.iterator().next();
	}

	public ProjectResult getProjectByPhid(String phid) {
		Collection<ProjectResult> qr = getProjectsByPhids(Arrays.asList(phid));
		return qr.isEmpty() ? null : qr.iterator().next();
	}

	public Collection<ProjectResult> getAllProjects() {
		return getProjectsByNames(null);
	}

	public Collection<ProjectResult> getAllActiveProjects() {
		try {
			return conduit.project.query(null, null, null, null, null, null, "satus-active", null, null, null).getData()
					.values();
		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<ProjectResult> getProjectsByPhids(List<String> phids) {
		try {
			return conduit.project.query(null, null, phids, null, null, null, null, null, null, null).getData()
					.values();
		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<ProjectResult> getProjectsByNames(List<String> names) {
		try {
			return conduit.project.query(null, names, null, null, null, null, null, null, null, null).getData()
					.values();
		} catch (ConduitException e) {
			throw new RuntimeException(e);
		}
	}

	public ConduitWrapper(Conduit conduit) {
		super();
		this.conduit = conduit;
	}
}
