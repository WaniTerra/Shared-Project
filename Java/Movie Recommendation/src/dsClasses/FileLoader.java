package dsClasses;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileLoader {

    private static MyLinkedList<Movie> movies = new MyLinkedList<>();

    public static MyLinkedList<User> loadUsers(String path) {
        MyLinkedList<User> users = new MyLinkedList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            br.readLine();

            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (count % 100 == 0) {
                    System.out.println(count + ". line reading");
                }

                String[] parts = line.split(",", -1);

                int userId = Integer.parseInt(parts[0].trim());
                User user = new User(userId);

                for (int i = 1; i < parts.length; i++) {
                    String val = parts[i].trim();
                    if (val.equals("")) {
                        val = "0";
                    }
                    user.addRating(Integer.parseInt(val));
                }

                users.add(user);
                count++;
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public static MyLinkedList<User> loadTargetUsers(String path) {
        MyLinkedList<User> targetUsers = new MyLinkedList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                int userId = Integer.parseInt(parts[0].trim());
                User user = new User(userId);

                for (int i = 1; i < parts.length; i++) {
                    String val = parts[i].trim();
                    if (val.equals("")) {
                        val = "0";
                    }
                    user.addRating(Integer.parseInt(val));
                }

                targetUsers.add(user);
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return targetUsers;
    }

    public static void loadMovies(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 3);

                if (parts.length < 2) {
                    continue;
                }

                int id = Integer.parseInt(parts[0].trim());
                String title = parts[1].trim();

                movies.add(new Movie(id, title));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMovieName(int index) {
        if (index < 0 || index >= movies.size()) {
            return "Unknown";
        }
        return movies.get(index).getName();
    }
}
