import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    public static User getUserById(URI uri, Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/" + String.valueOf(id)))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static User getUserByUserName(URI uri, String username) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "?username=" + username))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<User> result = GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
        return result.get(0);
    }

    public static List<User> getUsers(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
    }

    public static User postUser(URI uri, User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json" )
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static void deleteUser(URI uri, Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/" + String.valueOf(id)))
                .header("Content-type", "application/json" )
                .method("DELETE", HttpRequest.BodyPublishers.ofString("" ))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response status of delete User is " + response.statusCode());
    }

    public static User putUser(URI uri, User user) throws IOException, InterruptedException {
        String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/" + String.valueOf(user.getId())))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json" )
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response status of put User is " + response.statusCode());
        return GSON.fromJson(response.body(), User.class);

    }

    public static List<Todo> getUserTodos(URI uri, Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "/" + String.valueOf(id) + "/todos?completed=false" ))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), new TypeToken<List<Todo>>() {
        }.getType());
    }

    public static void getUserComments(Integer userId) throws IOException, InterruptedException {
        int maxPostId = getMaxPostID(userId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/" + String.valueOf(maxPostId) + "/comments" ))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Comment> comments = GSON.fromJson(response.body(), new TypeToken<List<Comment>>() {
        }.getType());
        writeCommentsToJson(comments, userId, maxPostId);
        System.out.println("Result wrote on file: " + "user-" + userId + "-post-" + maxPostId + "-comments.json");
    }

    private static int getMaxPostID(Integer userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/" + String.valueOf(userId) + "/posts" ))
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Post> posts = GSON.fromJson(response.body(), new TypeToken<List<Post>>() {
        }.getType());
        Integer maxPostId = posts.stream()
                .map(Post::getId)
                .max(Integer::compare)
                .get();
        return maxPostId;
    }

    private static void writeCommentsToJson(List<Comment> comments, Integer userId, int maxPostId) {
        try (PrintWriter out = new PrintWriter(
                new FileWriter("user-" + userId + "-post-" + maxPostId + "-comments.json" ))) {
            out.write(GSON.toJson(comments));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
