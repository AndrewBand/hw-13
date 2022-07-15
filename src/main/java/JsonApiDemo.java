import java.io.IOException;
import java.net.URI;
import java.util.List;

public class JsonApiDemo {

    private static final String POSTS = "https://jsonplaceholder.typicode.com/posts";
    private static final String USERS = "https://jsonplaceholder.typicode.com/users";


    public static void main(String[] args) throws IOException, InterruptedException {
        User newUser = new User(1, "Andr", "Kod",
                "acod@email", new Address("street100", "4", "Sumy",
                "457812", new Geo("01.0000", "02.0000")),
                "0506548585","acod.com",
                new Company("Luxoft", "the best of the best",
                        "harness real-time e-markets"));
        System.out.println("1.1 создание нового объекта" );
        User createdUser = HttpUtil.postUser(URI.create(USERS), newUser);
        System.out.println(createdUser);
        System.out.println("1.2 обновление объекта" );
        newUser.setUsername("Bam");
        User updatedUser = HttpUtil.putUser(URI.create(USERS), newUser);
        System.out.println(updatedUser);
        System.out.println("1.3 удаление объекта" );
        HttpUtil.deleteUser(URI.create(USERS), 11);
        System.out.println("1.4 получение информации обо всех пользователях" );
        List<User> allUsers = HttpUtil.getUsers(URI.create(USERS));
        System.out.println(allUsers);
        System.out.println("1.5 получение информации о пользователе с определенным id");
        User userById = HttpUtil.getUserById(URI.create(USERS),110);
        System.out.println(userById);
        System.out.println("1.6 получение информации о пользователе с определенным username" );
        User userName = HttpUtil.getUserByUserName(URI.create(USERS), "Delphine");
        System.out.println(userName);
        System.out.println("2 все комментарии к последнему посту определенного пользователя" );
        HttpUtil.getUserComments(2);
        System.out.println("3 все открытые задачи для пользователя" );
        List<Todo> allTodos = HttpUtil.getUserTodos(URI.create(USERS), 1);
        System.out.println(allTodos);

    }
}
