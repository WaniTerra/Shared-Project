package datastructures;

import dsClasses.*;
import java.io.*;

public class RecommendationEngine {

    private MyLinkedList<User> users;

    private MyLinkedList<Movie> movies;

    private MyMaxHeap heap;

    public RecommendationEngine(MyLinkedList<User> users) {

        this.users = users;

        this.movies = new MyLinkedList<>();

        this.heap = new MyMaxHeap();

    }

    public double cosine(User a, User b) {

        double dot = 0, na = 0, nb = 0;

        Node<Integer> nodeA = a.getRatings().getHead();

        Node<Integer> nodeB = b.getRatings().getHead();

        while (nodeA != null && nodeB != null) {

            int x = nodeA.data;

            int y = nodeB.data;

            dot += x * y;

            na += x * x;

            nb += y * y;

            nodeA = nodeA.next;

            nodeB = nodeB.next;

        }

        if (na == 0 || nb == 0) {

            return 0;

        }

        return dot / (Math.sqrt(na) * Math.sqrt(nb));

    }

    public void loadMovies(String path) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(path));

            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",", 3);

                movies.add(new Movie(
                        Integer.parseInt(p[0]),
                        p[1]
                ));

            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public String getMovie(int index) {

        if (index < 0 || index >= movies.size()) {

            return "Unknown";

        }

        return movies.get(index).getName();

    }

    public void build(User target) {

        this.heap = new MyMaxHeap();

        Node<User> currentUserNode = users.getHead();

        while (currentUserNode != null) {

            User u = currentUserNode.data;

            if (u.getUserId() != target.getUserId()) {

                heap.insert(new UserSimilarity(u, cosine(target, u)));

            }

            currentUserNode = currentUserNode.next;

        }

    }

    public User findUserById(MyLinkedList<User> userList, int id) {

        Node<User> current = userList.getHead();

        while (current != null) {

            if (current.data.getUserId() == id) {

                return current.data;

            }

            current = current.next;

        }

        return null;

    }

    // ---------------- RECOMMEND ----------------
    // ---------------- A PART ----------------
    public MyLinkedList<String> recommend(User target, int k, int x) {
        build(target);

        MyLinkedList<String> result = new MyLinkedList<>();

        MyLinkedList<Integer> targetWatched = new MyLinkedList<>();
        Node<Integer> targetRatingNode = target.getRatings().getHead();
        int m = 0;
        while (targetRatingNode != null) {
            if (targetRatingNode.data > 0) {
                targetWatched.add(m);
            }
            targetRatingNode = targetRatingNode.next;
            m++;
        }

        MyLinkedList<Integer> alreadyRecommended = new MyLinkedList<>();

        for (int i = 0; i < k; i++) {
            UserSimilarity us = heap.extractMax();
            if (us == null) {
                break;
            }

            User u = us.user;
            int count = 0;

            for (int ratingTarget = 5; ratingTarget >= 1 && count < x; ratingTarget--) {
                Node<Integer> currentRating = u.getRatings().getHead();
                int j = 0;

                while (currentRating != null) {
                    if (currentRating.data == ratingTarget
                            && !targetWatched.contains(j)
                            && !alreadyRecommended.contains(j)) {

                        result.add(getMovie(j));
                        alreadyRecommended.add(j);
                        count++;

                        if (count == x) {
                            break;
                        }
                    }
                    currentRating = currentRating.next;
                    j++;
                }
            }
        }

        return result;
    }

    // ---------------- B PART ----------------
    public MyLinkedList<Movie> getRandomMovies(int count) {

        MyLinkedList<Movie> randomList = new MyLinkedList<>();

        if (movies.size() <= count) {
            return movies;
        }

        int addedCount = 0;
        while (addedCount < count) {

            int randomIndex = (int) (Math.random() * movies.size());
            Movie randomMovie = movies.get(randomIndex);

            /* This part is only for presentation. Otherwise getting different recomendations hard. */
            /*
            int voteCount = 0;
            Node<User> tempUserNode = users.getHead();
            
            while (tempUserNode != null) {
                if (tempUserNode.data.getRatings().get(randomIndex) > 0) {
                    voteCount++;
                }
                tempUserNode = tempUserNode.next;
            }
            
            if (voteCount < 15) {
                continue; 
            }
            */
            
           /* ----------------------------------------------------------------------------- */

            boolean isAlreadyAdded = false;
            Node<Movie> current = randomList.getHead();

            while (current != null) {

                if (current.data.getId() == randomMovie.getId()) {
                    isAlreadyAdded = true;
                    break;
                }

                current = current.next;
            }

            if (!isAlreadyAdded) {
                randomList.add(randomMovie);
                addedCount++;
            }
        }

        return randomList;
    }

    public User createNewUserFromRatings(int newUserId, MyLinkedList<Integer> votedMovieIds, MyLinkedList<Integer> votedRatings) {

        User newUser = new User(newUserId);

        int totalMovies = users.getHead().data.getRatings().size();

        for (int i = 0; i < totalMovies; i++) {

            newUser.addRating(0);

        }

        Node<Integer> movieIdNode = votedMovieIds.getHead();

        Node<Integer> ratingNode = votedRatings.getHead();

        while (movieIdNode != null && ratingNode != null) {

            int movieId = movieIdNode.data;

            int rating = ratingNode.data;

            int movieIndex = -1;

            Node<Movie> currentMovie = movies.getHead();

            int j = 0;

            while (currentMovie != null) {

                if (currentMovie.data.getId() == movieId) {

                    movieIndex = j;

                    break;

                }

                currentMovie = currentMovie.next;

                j++;

            }

            if (movieIndex != -1) {

                newUser.getRatings().set(movieIndex, rating);

            }

            movieIdNode = movieIdNode.next;

            ratingNode = ratingNode.next;

        }

        return newUser;

    }

    // ---------------- For GUI----------------
    public static MyLinkedList<String> getUserRecommendation(MyLinkedList<User> users, MyLinkedList<User> targetUsers, int targetNodeId, int userNumber, int movieNumber) {

        RecommendationEngine engine = new RecommendationEngine(users);

        engine.loadMovies("src\\datastructures\\movies.csv");

        Node<User> current = targetUsers.getHead();

        while (current != null) {

            if (current.data.getUserId() == targetNodeId) {

                break;

            }

            current = current.next;

        }

        if (current == null) {

            return new MyLinkedList<>();

        }

        return engine.recommend(current.data, userNumber, movieNumber);

    }

    public static MyLinkedList<String> getCustomRatingRecommendation(
            MyLinkedList<User> users,
            MyLinkedList<Integer> votedMovieIds,
            MyLinkedList<Integer> votedRatings,
            int userNumber, int movieNumber) {

        RecommendationEngine engine = new RecommendationEngine(users);
        engine.loadMovies("src\\datastructures\\movies.csv");

        User simulatedUser = engine.createNewUserFromRatings(9999, votedMovieIds, votedRatings);

        return engine.recommend(simulatedUser, userNumber, movieNumber);
    }

    

}
