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

@WebServlet("/delete")
public class Delete extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();

        Person person = new Gson().fromJson(request.getReader().readLine(), Person.class);

        String db = "jdbc:mysql://localhost:3306/####"; // #### => Your database name
        try (
                Connection dbConnection = DriverManager.getConnection(db, "root", "####"); // root => username / #### => password
                Statement statement = dbConnection.createStatement()
                ) {

            String delete = "delete from #### where nationalId = '" + person.nationalId + "'" ; // #### => Table name
            int countDeleted = statement.executeUpdate(delete);
            jsonObject.addProperty("count deleted", countDeleted);

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
        String nationalId;

        public Person(String nationalId){
            this.nationalId = nationalId;
        }

        public String getNationalId() {
            return nationalId;
        }

        public void setNationalId(String nationalId) {
            this.nationalId = nationalId;
        }
    }
}
