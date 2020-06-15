package be.pxl.auctions.servlets;

import be.pxl.auctions.dao.AuctionDao;
import be.pxl.auctions.dao.impl.AuctionDaoImpl;
import be.pxl.auctions.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ActiveAuctionServlet", value = "/rest/count")
public class ActiveAuctionServlet extends HttpServlet {

    private AuctionDao auctionDao;
    private int activeAuctions;

    @Override
    public void init() throws ServletException {
        super.init();
        EntityManager entityManager = EntityManagerUtil.createEntityManager();
        auctionDao = new AuctionDaoImpl(entityManager);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        activeAuctions = auctionDao.findAllAuctions().size();
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
