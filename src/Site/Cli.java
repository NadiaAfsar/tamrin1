package Site;

import Model.Admin;
import Model.Faculty.Faculty;
import Model.Student;
import Model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cli {
    private User user;
    private Map<String, User> mapOfUsers;
    private ArrayList<User> users;
    private String username;
    private String password;
    private String pick;
    Scanner sc = new Scanner(System.in);
    private String firstName;
    private String lastName;
    private StudentCli studentCli;
    private AdminCli adminCli;
    private final Faculty MathematicalSciences;
    private final Faculty Language;
    private final Faculty Physics;
    private final Faculty ElectricalEngineering;
    public Cli(Faculty f1, Faculty f2, Faculty f3, Faculty f4) throws FileNotFoundException, IOException{
        this.MathematicalSciences = f1;
        this.Language = f2;
        this.Physics = f3;
        this.ElectricalEngineering = f4;
    }
    public void init () throws IOException {
        System.out.println("Welcome!\n1-Log in\n2-Sign up");
        pick = sc.next();
        if (pick.equals("1")) {
            this.LogIn();
        } else if (pick.equals("2")) {
            SignUp();
        } else {
            System.out.println("Invalid request!");
            init();
        }
    }
    private void LogIn () throws IOException{
        System.out.println("*-Back\nEnter your username (Your username is your student ID if you are a student):");
        username = sc.next();
        if (username.equals("*")) {
            init();
        }
        else if (mapOfUsers.get(username) != null) {
            getPassword();
        }
        else {
            System.out.println("This username doesn't exist.");
            this.LogIn();
        }
    }
    private void getPassword() throws FileNotFoundException, IOException{
        System.out.println("*-Back\nEnter your password:");
        password = sc.next();
        if (password.equals("*")) {
            LogIn();
        }
        else if (password.equals(mapOfUsers.get(username).getPassword())){
            if (username.equals("Admin")) {
                adminCli = new AdminCli(MathematicalSciences, Language, Physics, ElectricalEngineering);
                adminCli.getCli().setMapOfUsers(this.mapOfUsers);
                adminCli.getCli().setUsers(this.getUsers());
                adminCli.init();
            }
            else {
                user = mapOfUsers.get(username);
                studentCli = new StudentCli((Student) user, MathematicalSciences, Language, Physics, ElectricalEngineering);
                studentCli.getCli().setMapOfUsers(this.getMapOfUsers());
                studentCli.getCli().setUsers(this.getUsers());
                studentCli.init();
            }
        }
        else {
            System.out.println("Wrong password!");
            this.getPassword();
        }
    }
    private void SignUp() throws IOException{
        System.out.println("*-Back\nEnter your first name:");
        firstName = sc.next();
        if (firstName.equals("*")) {
            init();
        }
        else if (CliHelper.isAlphabetic(firstName)){
            getLastName();
        }
        else {
            System.out.println("Invalid first name!");
            SignUp();
        }
    }
    private void setPassword() throws IOException{
        System.out.println("*-Back\nEnter your password (Your password needs to be 9 characters):");
        password = sc.next();
        if (password.equals("*")) {
            SignUp();
        }
        else if (password.length() == 9) {
            repeatPassword();
        }
        else {
            System.out.println("Invalid password!");
        }
    }
    private void repeatPassword() throws IOException{
        System.out.println("*-Back\nEnter your password again:");
        String pass = sc.next();
        if (pass.equals("*")) {
            setPassword();
        }
        else if (pass.equals(password)) {
            user = new Student(username, password, firstName, lastName);
            mapOfUsers.put(username, user);
            users.add(user);
            SaveLoad.saveUsers(users);
            studentCli = new StudentCli((Student)user, MathematicalSciences, Language, Physics, ElectricalEngineering);
            studentCli.getCli().setMapOfUsers(this.getMapOfUsers());
            studentCli.getCli().setUsers(this.getUsers());
            studentCli.init();
        }
        else {
            System.out.println("Passwords don't match!");
            repeatPassword();
        }
    }
    private void setUsername () throws IOException{
        System.out.println("*-Back\nEnter your username (Student ID):");
        username = sc.next();
        if (username.equals("*")) {
            getLastName();
        }
        else if (mapOfUsers.get(username) == null){
            setPassword();
        }
        else {
            System.out.println("This username is taken.");
            setUsername();
        }
    }
    private void getLastName () throws IOException{
        System.out.println("*-Back\nEnter your last name:");
        lastName = sc.next();
        if (firstName.equals("*")) {
            SignUp();
        }
        else if (CliHelper.isAlphabetic(lastName)) {
            setUsername();
        }
        else {
            System.out.println("Invalid last name!");
            getLastName();
        }
    }
    public void loadData()  throws FileNotFoundException, IOException {
        users = new ArrayList<>();
        File file = new File("users");
        if (file.exists()) {
            SaveLoad.loadUsers(users, mapOfUsers);
            SaveLoad.loadCourses(MathematicalSciences, mapOfUsers);
            SaveLoad.loadCourses(Language, mapOfUsers);
            SaveLoad.loadCourses(Physics, mapOfUsers);
            SaveLoad.loadCourses(ElectricalEngineering, mapOfUsers);
        }
        else {
            Student st = new Student("402100356", "402100356", "nadia", "afsar");
            mapOfUsers.put("402100356", st);
            users.add(st);
            SaveLoad.saveUsers(users);
            SaveLoad.saveCourses(MathematicalSciences);
            SaveLoad.saveCourses(Language);
            SaveLoad.saveCourses(Physics);
            SaveLoad.saveCourses(ElectricalEngineering);
        }
        mapOfUsers.put("Admin", new Admin());
    }

    public Map<String, User> getMapOfUsers() {
        return mapOfUsers;
    }

    public void setMapOfUsers(Map<String, User> mapOfUsers) {
        this.mapOfUsers = mapOfUsers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentCli getStudentCli() {
        return studentCli;
    }

    public void setStudentCli(StudentCli studentCli) {
        this.studentCli = studentCli;
    }

    public AdminCli getAdminCli() {
        return adminCli;
    }

    public void setAdminCli(AdminCli adminCli) {
        this.adminCli = adminCli;
    }

    public Faculty getMathematicalSciences() {
        return MathematicalSciences;
    }

    public Faculty getLanguage() {
        return Language;
    }

    public Faculty getPhysics() {
        return Physics;
    }

    public Faculty getElectricalEngineering() {
        return ElectricalEngineering;
    }
}