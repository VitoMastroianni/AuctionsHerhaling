package be.pxl.auctions.servlets;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.impl.AuctionDaoImpl;
import be.pxl.auctions.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ActiveAuctionServlet", value = "/rest/count")
public class ActiveAuctionServlet extends HttpServlet {
    private EntityManager entityManager;
    private AuctionDao auctionDao;

    @Override
    public void init() throws ServletException {
        super.init();
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) getServletContext().getAttribute("entityManagerFactory");
        entityManager = EntityManagerUtil.createEntityManager();
        auctionDao = new AuctionDaoImpl(entityManager);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int activeAuctions = auctionDao.findAllAuctions().size();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        if (activeAuctions == 1){
            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<body>");
                out.println("<h1>Er is momenteel " + activeAuctions + " veiling actief</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<body>");
                out.println("<h1>Er zijn momenteel " + activeAuctions + " veilingen actief</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }

    }
}
