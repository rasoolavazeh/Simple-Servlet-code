import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@WebServlet("/insert")
public class Insert extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();

        Person person = new Gson().fromJson(request.getReader().readLine(), Person.class);

        String firstName = person.getFirstName();
        String lastName = person.getLastName();
        String nationalId = person.getNationalId();

        String db = "jdbc:mysql://localhost:3306/####"; // #### => Your database name
        try (
                Connection dbConnection = DriverManager.getConnection(db, "root", "####"); // root => username / #### => password
                Statement statement = dbConnection.createStatement()
                ) {
            String add = "insert into #### (firstName, lastName, nationalId) values ('" +
                    firstName + "', '" + lastName + "', '" + nationalId + "')"; // #### => Table name

            int countInserted = statement.executeUpdate(add);
            jsonObject.addProperty("count inserted", countInserted);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.println(jsonObject.toString());
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private class Person {
        String firstName;
        String lastName;
        String nationalId;

        public Person(String firstName, String lastName, String nationalId){
            this.firstName = firstName;
            this.lastName = lastName;
            this.nationalId = nationalId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getNationalId() {
            return nationalId;
        }

        public void setNationalId(String nationalId) {
            this.nationalId = nationalId;
        }
    }
}
