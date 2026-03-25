import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Participant Class
class Participant {
    private String name;
    private String email;

    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String toString() {
        return name + " (" + email + ")";
    }
}

// Event Class
class Event {
    private String title;
    private String date;
    private String category;
    private ArrayList<Participant> participants;

    public Event(String title, String date, String category) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.participants = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getCategory() { return category; }

    public void addParticipant(Participant p) {
        participants.add(p);
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public String toString() {
        return title + " (" + category + ") on " + date +
               " | Participants: " + participants.size();
    }
}

// Main GUI Class
public class EventManagementGUI extends JFrame {

    private DefaultListModel<Event> eventListModel;
    private JList<Event> eventList;
    private JTextArea participantArea;
    private ArrayList<Event> allEvents;
    private JComboBox<String> filterBox;
    private JTextField searchField;

    public EventManagementGUI() {
        setTitle("Event Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        allEvents = new ArrayList<>();
        eventListModel = new DefaultListModel<>();
        eventList = new JList<>(eventListModel);

        participantArea = new JTextArea();
        participantArea.setEditable(false);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Events", createEventPanel());
        tabbedPane.add("Participants", createParticipantPanel());

        add(tabbedPane);
    }

    // Event Panel
    private JPanel createEventPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Top Filter Panel
        JPanel topPanel = new JPanel();

        String[] filters = {"All", "Workshop", "Seminar", "Sports", "Cultural", "Other"};
        filterBox = new JComboBox<>(filters);
        filterBox.addActionListener(e -> applyFilter());

        searchField = new JTextField(15);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                applyFilter();
            }
        });

        topPanel.add(new JLabel("Category:"));
        topPanel.add(filterBox);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);

        panel.add(topPanel, BorderLayout.NORTH);

        panel.add(new JScrollPane(eventList), BorderLayout.CENTER);

        // Bottom Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        JTextField titleField = new JTextField();
        JTextField dateField = new JTextField();

        String[] categories = {"Workshop", "Seminar", "Sports", "Cultural", "Other"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);

        JButton addBtn = new JButton("Add Event");

        addBtn.addActionListener(e -> {
            String title = titleField.getText();
            String date = dateField.getText();
            String category = (String) categoryBox.getSelectedItem();

            if (!title.isEmpty() && !date.isEmpty()) {
                Event ev = new Event(title, date, category);
                allEvents.add(ev);
                applyFilter();

                titleField.setText("");
                dateField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
            }
        });

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryBox);
        inputPanel.add(addBtn);

        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Participant Panel
    private JPanel createParticipantPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        JButton registerBtn = new JButton("Register");

        registerBtn.addActionListener(e -> {
            Event ev = eventList.getSelectedValue();

            if (ev != null) {
                String name = nameField.getText();
                String email = emailField.getText();

                if (!name.isEmpty() && !email.isEmpty()) {
                    ev.addParticipant(new Participant(name, email));
                    updateParticipants(ev);

                    nameField.setText("");
                    emailField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Fill all fields!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an event first!");
            }
        });

        top.add(new JLabel("Name:"));
        top.add(nameField);
        top.add(new JLabel("Email:"));
        top.add(emailField);
        top.add(registerBtn);

        panel.add(top, BorderLayout.NORTH);

        panel.add(new JScrollPane(participantArea), BorderLayout.CENTER);

        eventList.addListSelectionListener(e -> {
            Event ev = eventList.getSelectedValue();
            if (ev != null) updateParticipants(ev);
        });

        return panel;
    }

    // Filter Logic
    private void applyFilter() {
        eventListModel.clear();

        String selected = (String) filterBox.getSelectedItem();
        String search = searchField.getText().toLowerCase();

        for (Event ev : allEvents) {
            boolean matchCategory = selected.equals("All") || ev.getCategory().equals(selected);
            boolean matchSearch = search.isEmpty()
                    || ev.getTitle().toLowerCase().contains(search)
                    || ev.getDate().toLowerCase().contains(search);

            if (matchCategory && matchSearch) {
                eventListModel.addElement(ev);
            }
        }
    }

    // Update Participant List
    private void updateParticipants(Event ev) {
        StringBuilder sb = new StringBuilder("Participants:\n");

        for (Participant p : ev.getParticipants()) {
            sb.append(p).append("\n");
        }

        participantArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EventManagementGUI().setVisible(true);
        });
    }
}