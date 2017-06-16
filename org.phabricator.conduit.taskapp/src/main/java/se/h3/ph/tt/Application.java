package se.h3.ph.tt;

import org.phabricator.conduit.ConduitException;
import org.phabricator.conduit.raw.ConduitFactory;
import org.phabricator.conduit.raw.ManiphestModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;

/**
 * Created by rene on 2017-06-11.
 */
public class Application {

    static final PopupMenu popup = new PopupMenu();
    ConduitWrapper cw;
    String usrPhid = "PHID-USER-mhf5xaj6o6zimcob7x7i";
    ClockTask ct = new ClockTask();

    public Application() {
        String token = "api-fgzgm3c7opso7z42auep5abfw47x";
        String url = "http://ph.labs.h3.se/";

        cw = new ConduitWrapper(ConduitFactory.createConduit(url, token));
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.

        Application a = new Application();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.createAndShowGUI();
            }
        });
    }

    Menu taskMenu = new Menu();

    /**
     * This loads the tasks from the server and refresh the list
     */
    void loadTasks() {

        taskMenu.removeAll();

        for (ManiphestModule.TaskResult tr : cw.getTasksForUser(usrPhid)) {
//            MenuItem mi = new MenuItem(tr.getTitle());
//            mi.addActionListener(e -> {
//                ct.startTask(tr.getPhid());
////                mi.setName(tr.getTitle());
////                mi.setLabel(tr.getDescription());
//
//                JOptionPane.showMessageDialog(null,
//                        "Starting the task: " + tr.getTitle());
//            });

            MenuItem mi = new TaskMenuEntry(tr);

            taskMenu.add(mi);
        }
    }

    private void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        final TrayIcon trayIcon =
                new TrayIcon(createImage("bulb.gif", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
//        MenuItem aboutItem = new MenuItem("About");
//        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
//        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
//        Menu displayMenu = new Menu("Display");
//        MenuItem errorItem = new MenuItem("Error");
//        MenuItem warningItem = new MenuItem("Warning");
//        MenuItem infoItem = new MenuItem("Info");
//        MenuItem noneItem = new MenuItem("None");
//        MenuItem exitItem = new MenuItem("Exit");

        {
            ct.startTask(null);
            popup.add(ct);
        }

        {
            MenuItem mi = new MenuItem("Update");
            mi.addActionListener(e -> {

                loadTasks();

//                ct.bookTime();
//
//                JOptionPane.showMessageDialog(null,
//                        "Stoping the task");
            });

            popup.add(mi);
        }



        loadTasks();

        popup.add(taskMenu);


//        //Add components to popup menu
//        popup.add(aboutItem);
//        popup.addSeparator();
//        popup.add(cb1);
//        popup.add(cb2);
//        popup.addSeparator();
//        popup.add(displayMenu);
//        displayMenu.add(errorItem);
//        displayMenu.add(warningItem);
//        displayMenu.add(infoItem);
//        displayMenu.add(noneItem);
//        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });

//        aboutItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null,
//                        "This dialog box is run from the About menu item");
//
//
//            }
//        });
//
//        cb1.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                int cb1Id = e.getStateChange();
//                if (cb1Id == ItemEvent.SELECTED) {
//                    trayIcon.setImageAutoSize(true);
//                } else {
//                    trayIcon.setImageAutoSize(false);
//                }
//            }
//        });
//
//        cb2.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                int cb2Id = e.getStateChange();
//                if (cb2Id == ItemEvent.SELECTED) {
//                    trayIcon.setToolTip("Sun TrayIcon");
//                } else {
//                    trayIcon.setToolTip(null);
//                }
//            }
//        });

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Error".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);

                } else if ("Warning".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);

                } else if ("Info".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.INFO;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);

                } else if ("None".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.NONE;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);
                }
            }
        };

//        errorItem.addActionListener(listener);
//        warningItem.addActionListener(listener);
//        infoItem.addActionListener(listener);
//        noneItem.addActionListener(listener);
//
//        exitItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                tray.remove(trayIcon);
//                System.exit(0);
//            }
//        });
    }

    //Obtain the image URL
    protected Image createImage(String path, String description) {
        URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    class TaskMenuEntry extends MenuItem {
        ManiphestModule.TaskResult tr;

        TaskMenuEntry(ManiphestModule.TaskResult tr) {
            addActionListener(e -> {
                ct.startTask(tr);
                JOptionPane.showMessageDialog(null,
                        "Starting the task: " + tr.getTitle());
            });

            setLabel(tr.getPriority() + " " + tr.getTitle());
        }
    }

    /**
     * I am the active task, when clicked you can start me, stop me. In the future you can complete med and complete me and check in.
     */
    class ClockTask extends MenuItem {

        String TIME_CONSTANT = "std:maniphest:se.h3.ph.tt.ms";

        Long start;

        ManiphestModule.TaskResult tr;

        public ClockTask() throws HeadlessException {
            addActionListener(e -> {
                if (tr == null) return;

                if (start == null) startTime();
                else bookTime();
            });
        }

        /**
         * Start working on a new task book time on old if there
         *
         * @param tr
         */
        void startTask(ManiphestModule.TaskResult tr) {

            if (this.tr != null) {
                bookTime();
            }

            this.tr = tr;

            if (tr == null) {
                setLabel("No task selected");
                return;
            }

            startTime();
        }

        /**
         * Start
         */
        void startTime() {
            start = minTime();
            setLabel("Working on: [" + tr.getPriority() + "] " + tr.getTitle());
        }

        long minTime() {
            return System.currentTimeMillis() / 60000;
        }

        /**
         * Book the time spent so far on this task
         */
        private void bookTime() {
            if (tr == null || start == null) return;

            ManiphestModule.TaskResult task = cw.getTaskByPhid(tr.getPhid());
            Map<String, String> am = task.getAuxiliary();
            String xt = am.get(TIME_CONSTANT);
            long ct = xt == null ? 0 : Long.parseLong(xt);
            long nt = ct + minTime() - start;

            start = null;
            setLabel("Stoped working on: [" + tr.getPriority() + "] " + tr.getTitle());

            if (nt > 0) {
                am.put(TIME_CONSTANT, "" + nt);
                try {
                    cw.conduit.getManiphestModule().update(null, tr.getPhid(), null, null, null, null, null, null, null, null, am, null, null);
                } catch (ConduitException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


