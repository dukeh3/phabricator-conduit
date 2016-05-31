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
package org.phabricator.conduit.demo;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionHandlerFilter;
import org.phabricator.conduit.ConduitException;
import org.phabricator.conduit.raw.Conduit;
import org.phabricator.conduit.raw.ConduitFactory;
import org.phabricator.conduit.raw.ConduitModule;
import org.phabricator.conduit.raw.ManiphestModule;

/**
 * Simple demo for phabricator-conduit
 */
public class Demo {
	@Option(name = "--help", aliases = { "-help", "-h", "-?", "help" }, help = true, usage = "print this help screen")
	private boolean help;

	@Option(name = "--url", required = true, usage = "url of the Phabricator "
			+ "instance to connect to (without the trailing \"/api/\"")
	private String url;

	@Option(name = "--username", usage = "username to use when connecting to " + "the Phabricator instance")
	private String username = null;

	@Option(name = "--certificate", usage = "certificate to use when " + "connecting to the Phabricator instance")
	private String certificate = null;

	private int taskId = -1;

	@Option(name = "--task", usage = "The task id to mangle during the demo " + "(without the leading \"T\")")
	private void setTaskId(String str) {
		if (str.startsWith("T")) {
			str = str.substring(1);
		}
		taskId = Integer.parseInt(str);
	}

	@Option(name = "--token", usage = "token to use when connecting to " + "the Phabricator instance")
	private String token = null;

	@Option(name = "--taskfile", usage = "filename of the taskfile")
	private String taskFileName = null;

	@Option(name = "--teamfile", usage = "filename of the teamfile")
	private String teamFileName = null;

	PrintStream stdout = System.out;
	PrintStream stderr = System.err;

	private void parseArgs(final String[] args) {
		final CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (final CmdLineException e) {
			stderr.println("Failed to parse args: " + e);
			e.printStackTrace(stderr);
			System.exit(1);
		}

		if (help) {
			// User asked for help screen
			parser.printUsage(stderr);
			stderr.println();
			stderr.print(parser.printExample(OptionHandlerFilter.REQUIRED));
			System.exit(0);
		}
	}

	private final String DASHES = "--------------------------------------"
			+ "-----------------------------------------";

	private Conduit conduit;

	private void logStart(String phase) {
		stdout.println();
		stdout.print("-- " + phase + " ");
		int printedChars = 3 + phase.length() + 1;
		if (printedChars < 80) {
			stdout.println(DASHES.substring(printedChars));
		}
	}

	private void logEnd() {
		stdout.println(DASHES);
		stdout.println("All demos ran.");
	}

	private boolean askPermission(String question) {
		stdout.println(question);
		stdout.println("(If yes, type in 'yes' (without quotes) and hit enter.)");

		Scanner scanner = new Scanner(System.in);
		String response = scanner.nextLine();
		scanner.close();

		stdout.println();

		return response != null && "yes".equals(response);
	}

	TeamList team;

	public void run(final String[] args) {
		parseArgs(args);

		try {
			conduit = token == null ? ConduitFactory.createConduit(url, username, certificate)
					: ConduitFactory.createConduit(url, token);

			logStart("conduit.ping (anonymous)"); // --------------------------------

			ConduitModule.PingResult pingResult = conduit.getConduitModule().ping();

			// The line above looks Java-ish by using a getter.
			//
			// If you prefer a Phabricator-ish look, you could instead use
			//
			// PingResult conduitPing = conduit.conduit.ping();
			//
			// which more closely resembles the called "conduit.ping"
			// Phabricator
			// Conduit method.

			stdout.println("conduit.ping returned hostname: " + pingResult.getHostname());

			// ----------------------------------------------------------------------
			if (taskId < 0) {
				logEnd();
				stdout.println("To run some more API calls we need a taskId");
				System.exit(0);
			}

			logStart("maniphest.info"); // ------------------------------------------

			// String ownerPhid = "PHID-USER-pn7h6mse3qmpoqhiulxk";
			// String viewPolicy = null;
			// String editPolicy = null;
			// List<String> ccPhids = new LinkedList<>();
			// Integer priority = 1;
			// List<String> projectPhids = new LinkedList<>();
			// projectPhids.add("PHID-PROJ-pjf5qp7inoktsnmn4tbk");
			//
			// Map<String, String> auxiliary = null;
			//
			// ManiphestModule.CreateTaskResult ctr =
			// conduit.getManiphestModule().createTask("testTask",
			// "This is a testdescription", ownerPhid, viewPolicy, editPolicy,
			// ccPhids, priority, projectPhids,
			// auxiliary);
			//
			// stdout.println("Fetching info about task T" + taskId + "...");

			ManiphestModule.InfoResult infoResult = conduit.getManiphestModule().info(taskId);

			stdout.println("Here it is:");
			stdout.println("  * title: " + infoResult.getTitle());
			stdout.println("  * created on: " + new Date(Long.parseLong(infoResult.getDateCreated()) * 1000));
			stdout.println("  * modified on: " + new Date(Long.parseLong(infoResult.getDateModified()) * 1000));
			stdout.println("  * priority: " + infoResult.getPriority());
			stdout.println("  * uri: " + infoResult.getUri());
			stdout.println("(Much more information is available. See the " + "InfoResult class)");

			System.out.println(infoResult.getAuthorPHID());
			System.out.println(infoResult.getOwnerPHID());
			System.out.println(infoResult.getPriority());
			System.out.println(infoResult.getProjectPHIDs().get(0));

			System.out.println("DEPENDENT");
			for (String x : infoResult.getDependsOnTaskPHIDs()) {
				System.out.println(x);
			}

			for (String key : infoResult.getAuxiliary().keySet()) {
				System.out.println(key + ":" + infoResult.getAuxiliary().get(key));
			}

			logStart("maniphest.update"); // ------------------------------------------

			// if (askPermission("May I add a comment saying 'Test comment' to
			// task T" + taskId + "?")) {
			// stdout.println("Adding 'Test comment' to task T" + taskId +
			// "...");
			// final ManiphestModule.UpdateResult updateResult =
			// conduit.getManiphestModule().update(taskId, null,
			// null, null, null, null, null, null, null, null, null, null, "Test
			// comment");
			// stdout.println("Done.");
			//
			// stdout.println("Fetching fresh info about task T" + taskId +
			// "...");
			// infoResult = conduit.getManiphestModule().info(taskId);
			// stdout.println("New task modification date is "
			// + new Date(Long.parseLong(updateResult.getDateModified()) *
			// 1000));
			// }

			try {
				JAXBContext carContext = JAXBContext.newInstance(Task.class);
				Unmarshaller carMarshaller = carContext.createUnmarshaller();
				Task unmarshal = (Task) carMarshaller.unmarshal(new FileReader(new File(taskFileName)));

				Unmarshaller teamUnMarshaller = JAXBContext.newInstance(Task.class).createUnmarshaller();
				team = (TeamList) teamUnMarshaller.unmarshal(new FileReader(new File(teamFileName)));

				rec(unmarshal, null);

			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}

			// List<ManiphestModule.AbstractEditTransaction> transactions = new
			// LinkedList<>();
			//
			// transactions.add(new
			// ManiphestModule.ParentTransaction("PHID-TASK-ufdkh2ltnlryy7pjkfz4"));
			// transactions.add(new
			// ManiphestModule.TitleTransaction("MyTitle"));
			// transactions.add(new
			// ManiphestModule.DescriptionTransaction("MyDescription"));
			// transactions.add(new
			// ManiphestModule.OwnerTransaction("PHID-USER-pn7h6mse3qmpoqhiulxk"));
			// transactions.add(new ManiphestModule.PriorityTransaction(50));
			// transactions.add(new ManiphestModule.ProjectsSetTransaction(
			// Arrays.asList(new String[] { "PHID-PROJ-pjf5qp7inoktsnmn4tbk"
			// })));
			//
			// conduit.getManiphestModule().editTask(transactions);

			logEnd(); // ------------------------------------------------------------
		} catch (Exception e) {
			stderr.println("Demo failed: " + e);
			e.printStackTrace(stderr);
			System.exit(1);
		}
	}

	@Option(name = "--priority", usage = "The prio")
	int priority = 50;
	
	@Option(name = "--projid", usage = "the projid")
	String projId = null; 
//	"PHID-PROJ-pjf5qp7inoktsnmn4tbk";

	void rec(Task t, String p) throws ConduitException {
		List<ManiphestModule.AbstractEditTransaction> transactions = new LinkedList<>();

		if (p != null)
			transactions.add(new ManiphestModule.ParentTransaction(p));

		String owner = t.cls != null ? team.members.get(t.cls) : null;
		// "PHID-USER-pn7h6mse3qmpoqhiulxk";

		transactions.add(new ManiphestModule.TitleTransaction(t.title));
		transactions.add(new ManiphestModule.DescriptionTransaction(t.description));

		if (owner != null)
			transactions.add(new ManiphestModule.OwnerTransaction(owner));

		transactions.add(new ManiphestModule.PriorityTransaction(priority));

		if (projId != null)
			transactions.add(new ManiphestModule.ProjectsSetTransaction(Arrays.asList(new String[] { projId })));

		 conduit.getManiphestModule().editTask(transactions);
		String res = "";

		for (Task st : t.subTasks) {
			rec(st, res);
		}
	}

	public static void main(final String[] args) {
		Demo demo = new Demo();
		demo.run(args);
	}
}
