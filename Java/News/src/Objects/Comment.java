package Objects;

import Panel.PublicMethods;
import SQL.Readerdb;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Comment implements Serializable {

    private static ArrayList<Comment> allComments = new ArrayList<>();
    private static int commentNumber = commentNumber();

    private int commentId;
    private String comment;
    private int like;
    private int dislike;
    private String ownerEmail;
    private String newsTitle;

    public Comment(int like, int dislike, String comment, boolean fromFile, String ownerEmail, int existingCommentId, String newsTitle) {
        this.like = like;
        this.dislike = dislike;
        this.comment = comment;
        this.ownerEmail = ownerEmail;
        this.newsTitle = newsTitle;

        if (fromFile) {
            this.commentId = existingCommentId;

            if (existingCommentId >= commentNumber) {
                commentNumber = existingCommentId + 1;
            }

        } else {
            this.commentId = commentNumber++;

            commentFileWriter(this);

            PublicMethods.readerFinderWithEmail(this.ownerEmail).getMyComments().add(this);
        }
        allComments.add(this);
    }

    // Getter and Setter metods
    public static ArrayList<Comment> getAllComments() {
        return allComments;
    }

    public static void setAllComments(ArrayList<Comment> allComments) {
        Comment.allComments = allComments;
    }

    public static int getCommentNumber() {
        return commentNumber;
    }

    public static void setCommentNumber(int commentNumber) {
        Comment.commentNumber = commentNumber;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like += like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike += dislike;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public static void commentDeleter(int commentId, News news) {
        for (int i = 0; i < allComments.size(); i++) {
            if (allComments.get(i).getCommentId() == commentId) {
                allComments.remove(i);
                break;
            }
        }
        for (int i = 0; i < news.getNewsComments().size(); i++) {
            if (news.getNewsComments().get(i).getCommentId() == commentId) {
                news.getNewsComments().remove(i);
                break;
            }
        }
        for (int i = 0; i < Reader.getAllReaders().size(); i++) {
            Reader reader = (Reader) Reader.getAllReaders().get(i);
            for (int j = 0; j < reader.getLikedComment().size(); j++) {
                if (reader.getLikedComment().get(j).getCommentId() == commentId) {
                    reader.getLikedComment().remove(j);
                    break;
                }

            }

            for (int j = 0; j < reader.getDislikedComment().size(); j++) {
                if (reader.getDislikedComment().get(j).getCommentId() == commentId) {
                    reader.getDislikedComment().remove(j);
                    break;
                }

            }

            for (int j = 0; j < reader.getMyComments().size(); j++) {
                if (reader.getMyComments().get(j).getCommentId() == commentId) {
                    reader.getMyComments().remove(j);
                    break;
                }

            }

        }
        deleteCommentFromFile(commentId);
    }

    public static void deleteCommentFromFile(int commentId) {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            boolean skipCurrentComment = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) {

                    int idStart = line.indexOf("ID: ") + 4;
                    int idEnd = line.indexOf("-", idStart);
                    int currentId = Integer.parseInt(line.substring(idStart, idEnd).trim());

                    skipCurrentComment = (currentId == commentId);
                }

                if (!skipCurrentComment) {
                    lines.add(line);
                } else if (line.startsWith("ID: ") && !skipCurrentComment) {

                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void likeCommentFromFile(int commentId, boolean liked) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            boolean skipCurrentComment = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) {
                    int idStart = line.indexOf("ID: ") + 4;
                    int idEnd = line.indexOf("-", idStart);
                    int currentId = Integer.parseInt(line.substring(idStart, idEnd).trim());

                    skipCurrentComment = (currentId == commentId);

                    if (skipCurrentComment) {
                        int likeStart = line.indexOf("Like: ") + 6;
                        int likeEnd = line.indexOf("-", likeStart);
                        int like = Integer.parseInt(line.substring(likeStart, likeEnd).trim());

                        if (liked) {
                            like++;
                        } else {
                            like--;
                        }

                        line = line.substring(0, likeStart) + like + line.substring(likeEnd);
                    }
                }

                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dislikeCommentFromFile(int commentId, boolean disliked) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            boolean skipCurrentComment = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) {
                    int idStart = line.indexOf("ID: ") + 4;
                    int idEnd = line.indexOf("-", idStart);
                    int currentId = Integer.parseInt(line.substring(idStart, idEnd).trim());

                    skipCurrentComment = (currentId == commentId);

                    if (skipCurrentComment) {
                        int dislikeStart = line.indexOf("Dislike: ") + 9;
                        int dislikeEnd = line.indexOf("-", dislikeStart);
                        int dislike = Integer.parseInt(line.substring(dislikeStart, dislikeEnd).trim());

                        if (disliked) {
                            dislike++;
                        } else {
                            dislike--;
                        }

                        line = line.substring(0, dislikeStart) + dislike + line.substring(dislikeEnd);
                    }
                }

                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editCommentFromFile(int commentId, String newValue) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("ID: ")) {

                int idStart = line.indexOf("ID: ") + 4;
                int idEnd = line.indexOf("-", idStart);
                int currentId = Integer.parseInt(line.substring(idStart, idEnd).trim());

                if (currentId == commentId) {

                    int likeStart = line.indexOf("Like: ") + 6;
                    int likeEnd = line.indexOf("-", likeStart);
                    String likeValue = line.substring(likeStart, likeEnd).trim();

                    int dislikeStart = line.indexOf("Dislike: ") + 9;
                    int dislikeEnd = line.indexOf("-", dislikeStart);
                    String dislikeValue = line.substring(dislikeStart, dislikeEnd).trim();

                    String email = "";
                    String newsTitle = "";
                    int nextCommentStart = findNextCommentStart(lines, i);

                    for (int j = i; j < nextCommentStart; j++) {
                        String currentLine = lines.get(j);
                        if (currentLine.contains("Email: ") && currentLine.contains("News: ")) {
                            int emailIdx = currentLine.indexOf("Email: ") + 7;
                            int newsIdx = currentLine.indexOf("News: ");
                            if (newsIdx > emailIdx) {
                                email = currentLine.substring(emailIdx, newsIdx).trim().replaceAll(",$", "");
                                newsTitle = currentLine.substring(newsIdx + 6).trim();
                            } else {
                                int emailEndIdx = currentLine.indexOf("Email: ");
                                newsTitle = currentLine.substring(newsIdx + 6, emailEndIdx).trim().replaceAll(",$", "");
                                email = currentLine.substring(emailIdx).trim();
                            }
                            break;
                        }
                    }

                    String newHeaderLine = "ID: " + commentId + ", Like: " + likeValue
                            + ", Dislike: " + dislikeValue + ", Comment: " + newValue;

                    String emailNewsLine = "Email: " + email + ", News: " + newsTitle;

                    int commentEndIndex = nextCommentStart;
                    for (int k = commentEndIndex - 1; k >= i; k--) {
                        lines.remove(k);
                    }

                    lines.add(i, newHeaderLine);
                    lines.add(i + 1, emailNewsLine);

                    break;
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj
    ) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Comment comment1 = (Comment) obj;
        return commentId == comment1.commentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(commentId);
    }

    public static void commentArrayFiller() {
        File file = new File("comments.txt");
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"))) {
                writer.write("commentID:0");
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
                return; 
            }
        }
        try (BufferedReader rd = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            List<String> allLines = new ArrayList<>();
            while ((line = rd.readLine()) != null) {
                allLines.add(line);
            }

            if (allLines.isEmpty()) {
                return;
            }

            if (allLines.get(0).startsWith("commentID:")) {
                allLines.remove(0);
            }

            for (int i = 0; i < allLines.size(); i++) {
                String currentLine = allLines.get(i);
                if (currentLine.startsWith("ID: ")) {
                    Comment comment = parseComment(allLines, i);
                    if (comment != null) {
                        PublicMethods.newsFinder(comment.newsTitle).getNewsComments().add(0, comment);
                    }
                    i = findNextCommentStart(allLines, i) - 1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Comment parseComment(List<String> lines, int startIndex) {
        if (startIndex >= lines.size()) {
            return null;
        }

        String headerLine = lines.get(startIndex);
        if (!headerLine.startsWith("ID: ")) {
            return null;
        }

        try {

            int idStart = headerLine.indexOf("ID: ") + 4;
            int idEnd = headerLine.indexOf("-", idStart);
            int id = Integer.parseInt(headerLine.substring(idStart, idEnd).trim());

            int likeStart = headerLine.indexOf("Like: ") + 6;
            int likeEnd = headerLine.indexOf("-", likeStart);
            int like = Integer.parseInt(headerLine.substring(likeStart, likeEnd).trim());

            int dislikeStart = headerLine.indexOf("Dislike: ") + 9;
            int dislikeEnd = headerLine.indexOf("-", dislikeStart);
            int dislike = Integer.parseInt(headerLine.substring(dislikeStart, dislikeEnd).trim());

            int commentStart = headerLine.indexOf("Comment: ") + 9;
            int emailStart = headerLine.indexOf("- Email: ");

            StringBuilder commentBuilder = new StringBuilder();

            if (emailStart > commentStart && emailStart != -1) {

                commentBuilder.append(headerLine.substring(commentStart, emailStart));
            } else {

                commentBuilder.append(headerLine.substring(commentStart));

                int nextCommentStart = findNextCommentStart(lines, startIndex);
                for (int i = startIndex + 1; i < nextCommentStart; i++) {
                    String line = lines.get(i);

                    if (line.contains("Email: ") && line.contains("News: ")) {

                        int emailIndex = line.indexOf("- Email: ");
                        if (emailIndex > 0) {
                            commentBuilder.append("\n").append(line.substring(0, emailIndex));
                        }
                        break;
                    } else {

                        commentBuilder.append("\n").append(line);
                    }
                }
            }

            String email = "";
            String newsTitle = "";

            int nextCommentStart = findNextCommentStart(lines, startIndex);
            for (int i = startIndex; i < nextCommentStart; i++) {
                String line = lines.get(i);
                if (line.contains("Email: ") && line.contains("News: ")) {
                    int emailIdx = line.indexOf("Email: ") + 7;
                    int newsIdx = line.indexOf("News: ");

                    if (newsIdx > emailIdx) {
                        email = line.substring(emailIdx, newsIdx).trim().replaceAll("-$", "");
                        newsTitle = line.substring(newsIdx + 6).trim();
                    } else {

                        int emailEndIdx = line.indexOf("Email: ");
                        newsTitle = line.substring(newsIdx + 6, emailEndIdx).trim().replaceAll("-$", "");
                        email = line.substring(emailIdx).trim();
                    }
                    break;
                }
            }

            return new Comment(like, dislike, commentBuilder.toString().trim(), true, email, id, newsTitle);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private static int findNextCommentStart(List<String> lines, int currentIndex) {
        for (int i = currentIndex + 1; i < lines.size(); i++) {
            if (lines.get(i).startsWith("ID: ")) {
                return i;
            }
        }
        return lines.size();
    }

    private static void commentFileWriter(Comment comment) {

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!lines.isEmpty() && lines.get(0).startsWith("commentID:")) {
            lines.set(0, "commentID:" + comment.commentId);
        } else {
            lines.add(0, "commentID:" + comment.commentId);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("comments.txt"))) {

            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }

            writer.write("ID: " + comment.commentId
                    + "- Like: " + comment.like
                    + "- Dislike: " + comment.dislike
                    + "- Comment: " + comment.comment
                    + "- Email: " + comment.ownerEmail
                    + "- News: " + comment.newsTitle);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int commentNumber() {
        int commentID = 0;
        try (BufferedReader rd = new BufferedReader(new FileReader("comments.txt"))) {
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.startsWith("commentID:")) {
                    String[] parts = line.split(":");
                    commentID = Integer.valueOf(parts[1].trim());
                    break;
                }
            }
        } catch (IOException e) {

            commentID = 0;
        }
        return commentID;
    }

}
