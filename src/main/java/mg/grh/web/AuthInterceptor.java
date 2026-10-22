package mg.grh.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/** Protège les espaces web selon le rôle conservé en session. */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HttpSession session = req.getSession(false);
        SessionUser user = session == null ? null : (SessionUser) session.getAttribute("user");
        String path = req.getRequestURI().substring(req.getContextPath().length());

        String requis = path.startsWith("/admin") || path.startsWith("/rh/") ? "RH"
                : path.startsWith("/dg") ? "DG"
                        : path.startsWith("/directeur") ? "DIRECTEUR"
                                : path.startsWith("/finance") ? "FINANCE"
                                        : path.startsWith("/candidat") || path.startsWith("/candidats/") ? "CANDIDAT"
                                                : "RH";

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        if (!requis.equals(user.getType())) {
            res.sendRedirect(req.getContextPath() + "/login?refuse");
            return false;
        }
        return true;
    }
}
