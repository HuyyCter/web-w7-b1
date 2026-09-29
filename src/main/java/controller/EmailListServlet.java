package controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import utils.MailUtil;

import java.io.IOException;

import dao.UserDAO;

/**
 * Servlet implementation class EmailListServlet
 */
@WebServlet("/EmailListServlet")
public class EmailListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmailListServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String url = "";
		String message = "";

		String action = request.getParameter("action");
		if (action == null) {
			action = "join";
		}

		if (action.equals("join")) {
			url = "/index.jsp";
		} else if (action.equals("add")) {
			String email = request.getParameter("email");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");

			User user = new User(firstName, lastName, email);

			if (UserDAO.isEmailExists(email)) {
				message = "This email address already exists.<br>" + "Please enter another email address.";
				url = "/index.jsp";
			} else {
				url = "/thanks.jsp";
				UserDAO.insert(user);

				// Send mail to user
				String to = email;
				String from = "onboarding@resend.dev";
				String subject = "Welcome to our email list";
				String body = "Dear " + firstName + ",\n\n" + "Thanks for joining our email list. " + "We'll make sure to send "
						+ "you announcements about new products " + "and promotions.\n" + "Have a great day and thanks again!\n\n"
						+ "Kelly Slivkoff\n" + "Mike Murach & Associates";
				boolean isBodyHtml = false;
				try {
					MailUtil.sendGmail(to, from, subject, body, isBodyHtml);
				} catch (Exception e) {
					this.log("Chi tiết lỗi gửi mail: " + e.getMessage(), e);
					String errorMessage = "ERROR: Unable to send email. " + "Check Tomcat logs for details.<br>"
							+ "NOTE: You may need to configure your system " + "as described in chapter 14.<br>" + "ERROR MESSAGE: " + e.getMessage();
					request.setAttribute("errorMessage", errorMessage);
					this.log("Unable to send email. \n" + "Here is the email you tried to send: \n" + "=====================================\n"
							+ "TO: " + email + "\n" + "FROM: " + from + "\n" + "SUBJECT: " + subject + "\n\n" + body + "\n\n");
				}
			}

			request.setAttribute("user", user);
			request.setAttribute("message", message);
		}

		request.getRequestDispatcher(url).forward(request, response);
	}

}
