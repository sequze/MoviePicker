package listener;

import com.zaxxer.hikari.HikariDataSource;
import dao.*;
import dao.impl.*;
import service.*;
import service.impl.*;
import util.DataSourceManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class AppContextListener implements ServletContextListener {
    private HikariDataSource dataSource;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        dataSource = DataSourceManager.init();

        // Создаём все DAO
        UserDao userDao = new UserDaoImpl(dataSource);
        MovieDao movieDao = new MovieDaoImpl(dataSource);
        GenreDao genreDao = new GenreDaoImpl(dataSource);
        DirectorDao directorDao = new DirectorDaoImpl(dataSource);
        UserWatchedDao userWatchedDao = new UserWatchedDaoImpl(dataSource);
        UserFavoriteDao userFavoriteDao = new UserFavoriteDaoImpl(dataSource);

        // Создаём сервисы
        UserService userService = new UserServiceImpl(userDao);
        MovieService movieService = new MovieServiceImpl(movieDao, genreDao, directorDao);
        UserWatchedService userWatchedService = new UserWatchedServiceImpl(userWatchedDao, movieDao);
        DirectorService directorService = new DirectorServiceImpl(directorDao);
        GenreService genreService = new GenreServiceImpl(genreDao);
        UserFavoriteService userFavoriteService = new UserFavoriteServiceImpl(userFavoriteDao);

        // Передаем созданные объекты в контекст сервлета
        Map<String, Object> services = new HashMap<>();
        services.put("userService", userService);
        services.put("movieService", movieService);
        services.put("userWatchedService", userWatchedService);
        services.put("directorService", directorService);
        services.put("genreService", genreService);
        services.put("userFavoriteService", userFavoriteService);

        Map<String, Object> daos = new HashMap<>();
        daos.put("userDao", userDao);
        daos.put("movieDao", movieDao);
        daos.put("genreDao", genreDao);
        daos.put("directorDao", directorDao);
        daos.put("userWatchedDao", userWatchedDao);
        daos.put("userFavoriteDao", userFavoriteDao);

        sce.getServletContext().setAttribute("services", services);
        sce.getServletContext().setAttribute("daos", daos);
        sce.getServletContext().setAttribute("dataSource", dataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
