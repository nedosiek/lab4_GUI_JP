package org.fitness_tracker.db;

import org.fitness_tracker.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:fitness_tracker.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
            initializeSampleDataIfEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void createTables(Connection conn) {
        String createExerciseTable = """
    CREATE TABLE IF NOT EXISTS EXERCISE (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        type TEXT NOT NULL
    );
    """;

        String createUserGoalTable = """
    CREATE TABLE IF NOT EXISTS USER_GOAL (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        exercise_id INTEGER,
        goal REAL,
        goal_deadline DATE,
        FOREIGN KEY (exercise_id) REFERENCES EXERCISE(id)
    );
    """;

        String createExerciseDetailTable = """
    CREATE TABLE IF NOT EXISTS EXERCISE_DETAIL (
        exerciseDetail_id INTEGER PRIMARY KEY AUTOINCREMENT,
        exercise_id INTEGER,
        sets INTEGER,
        value REAL,
        unit TEXT,
        FOREIGN KEY (exercise_id) REFERENCES EXERCISE(id)
    );
    """;

        String createWorkoutTable = """
    CREATE TABLE IF NOT EXISTS WORKOUT (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL
    );
    """;

        String createTrainSessionTable = """
    CREATE TABLE IF NOT EXISTS TRAIN_SESSION (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        date DATE,
        workout_id INTEGER,
        FOREIGN KEY (workout_id) REFERENCES WORKOUT(id)
    );
    """;

        String createWorkoutExerciseTable = """
    CREATE TABLE IF NOT EXISTS WORKOUT_EXERCISE (
        workout_id INTEGER,
        exercise_detail_id INTEGER,
        PRIMARY KEY (workout_id, exercise_detail_id),
        FOREIGN KEY (workout_id) REFERENCES WORKOUT(id),
        FOREIGN KEY (exercise_detail_id) REFERENCES EXERCISE_DETAIL(exercise_id)
    );
    """;

        try (var stmt = conn.createStatement()) {
            stmt.execute(createExerciseTable);
            stmt.execute(createUserGoalTable);
            stmt.execute(createExerciseDetailTable);
            stmt.execute(createWorkoutTable);
            stmt.execute(createTrainSessionTable);
            stmt.execute(createWorkoutExerciseTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initializeSampleDataIfEmpty() {
        try (Connection conn = getConnection()) {
            // Sprawdź, czy tabela EXERCISE jest pusta
            String checkExerciseTable = "SELECT COUNT(*) FROM EXERCISE";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkExerciseTable)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertExercises = """
                INSERT INTO EXERCISE (name, type) VALUES
                ('Pompki', 'Strength'),
                ('Przysiady', 'Strength'),
                ('Bieg', 'Cardio');
            """;
                    stmt.execute(insertExercises);
                }
            }

            String checkExerciseDetailTable = "SELECT COUNT(*) FROM EXERCISE_DETAIL";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkExerciseDetailTable)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertExerciseDetails = """
                INSERT INTO EXERCISE_DETAIL (exercise_id, sets, value, unit) VALUES
                (1, 3, 15, 'reps'),  -- Pompki
                (2, 3, 20, 'reps'),  -- Przysiady
                (3, 1, 5, 'km');     -- Bieg
            """;
                    stmt.execute(insertExerciseDetails);
                }
            }

            String checkWorkoutTable = "SELECT COUNT(*) FROM WORKOUT";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkWorkoutTable)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertWorkouts = """
                INSERT INTO WORKOUT (name) VALUES
                ('Poranny trening');
            """;
                    stmt.execute(insertWorkouts);
                }
            }

            String checkTrainSessionTable = "SELECT COUNT(*) FROM TRAIN_SESSION";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkTrainSessionTable)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertTrainSessions = """
                INSERT INTO TRAIN_SESSION (workout_id, date) VALUES
                (1, '2023-10-01');
            """;
                    stmt.execute(insertTrainSessions);
                }
            }

            String checkUserGoalTable = "SELECT COUNT(*) FROM USER_GOAL";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkUserGoalTable)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertUserGoals = """
                INSERT INTO USER_GOAL (exercise_id, goal, goal_deadline) VALUES
                (1, 50, '2023-12-31'),
                (2, 100, '2023-12-31');
            """;
                    stmt.execute(insertUserGoals);
                }
            }

            String checkWorkoutExerciseTable = "SELECT COUNT(*) FROM WORKOUT_EXERCISE";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkWorkoutExerciseTable)) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String insertWorkoutExercises = """
                INSERT INTO WORKOUT_EXERCISE (workout_id, exercise_detail_id) VALUES
                (1, 1),
                (1, 2),
                (1, 3);
            """;
                    stmt.execute(insertWorkoutExercises);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addExercise(String name, String type) {
        String sql = "INSERT INTO EXERCISE (name, type) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Exercise> getExercises() {
        ArrayList<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT id,name,type FROM EXERCISE";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                exercises.add(new Exercise(rs.getInt("id"),rs.getString("name"),rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }
    public static void updateExercise(Exercise ex) {
        String updateQuery = "UPDATE EXERCISE SET name = ?, type = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, ex.getName());
            pstmt.setString(2, ex.getType());
            pstmt.setInt(3, ex.getExerciseID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteExercise(int id) {
        String sql = "DELETE FROM EXERCISE WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addWorkoutSession(int workoutId, String date) {
        String sql = "INSERT INTO TRAIN_SESSION (workout_id, date) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workoutId);
            pstmt.setString(2, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TrainingSession> getWorkoutSessions() {
        ArrayList<TrainingSession> sessions = new ArrayList<>();
        String sql = "SELECT t.id,t.date,t.workout_id,w.name FROM TRAIN_SESSION t join WORKOUT w on t.workout_id = w.id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int wID = Integer.parseInt(rs.getString("workout_id"));
                int id = Integer.parseInt(rs.getString("id"));
                String wName = rs.getString("name");
                Workout workout = new Workout(wID,wName);
                LocalDate date = LocalDate.parse(rs.getString("date"));
                sessions.add(new TrainingSession(id,date,workout));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    public static void addUserGoal(int exerciseId, float goal, String deadline) {
        String sql = "INSERT INTO USER_GOAL (exercise_id, goal, goal_deadline) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, exerciseId);
            pstmt.setFloat(2, goal);
            pstmt.setString(3, deadline);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Goals> getUserGoals() {
        ArrayList<Goals> goals = new ArrayList<>();
        String sql = """
    SELECT g.id, g.exercise_id, g.goal, g.goal_deadline, e.name, e.type FROM USER_GOAL g
    JOIN EXERCISE e ON g.exercise_id = e.id
    """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = Integer.parseInt(rs.getString("id"));
                Exercise ex1 = new Exercise(rs.getInt("exercise_id"), rs.getString("name"), rs.getString("type"));
                float goalValue = rs.getFloat("goal");
                LocalDate deadline = LocalDate.parse(rs.getString("goal_deadline"));
                goals.add(new Goals(id, ex1, goalValue, deadline));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goals;
    }
    public static void deleteUserGoal(int id) {
        String sql = "DELETE FROM USER_GOAL WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void addExerciseDetail(int exerciseId, int sets, float value, String unit) {
        String sql = "INSERT INTO EXERCISE_DETAIL (exercise_id, sets, value, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, exerciseId);
            pstmt.setInt(2, sets);
            pstmt.setFloat(3, value);
            pstmt.setString(4, unit);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ExerciseDetail> getExerciseDetails() {
        ArrayList<ExerciseDetail> details = new ArrayList<>();
        String sql = """
        SELECT ed.exerciseDetail_id, ed.exercise_id, ed.sets, ed.value, ed.unit, e.name, e.type FROM EXERCISE_DETAIL ed
        JOIN EXERCISE e ON ed.exercise_id = e.id
    """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Exercise ex = new Exercise(rs.getInt("exercise_id"), rs.getString("name"), rs.getString("type"));
                details.add(new ExerciseDetail(rs.getInt("exerciseDetail_id"), ex, rs.getInt("sets"), rs.getString("unit"), rs.getFloat("value")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static void updateExerciseDetail(int exerciseDetailId, int sets, float value, String unit) {
        String sql = "UPDATE EXERCISE_DETAIL SET sets = ?, value = ?, unit = ? WHERE exerciseDetail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sets);
            pstmt.setFloat(2, value);
            pstmt.setString(3, unit);
            pstmt.setInt(4, exerciseDetailId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteExerciseDetail(int id) {
        String sql = "DELETE FROM EXERCISE_DETAIL WHERE exerciseDetail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteExerciseDetailbyExercise(int id) {
        String sql = "DELETE FROM EXERCISE_DETAIL WHERE exercise_detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteExerciseDetailInWE(int id) {
        String sql = "DELETE FROM WORKOUT_EXERCISE WHERE exercise_detail_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUserGoal(int id, float goal, String deadline) {
        String sql = "UPDATE USER_GOAL SET goal = ?, goal_deadline = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, goal);
            pstmt.setString(2, deadline);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWorkoutSessions(int id) {
        String sql = "DELETE FROM TRAIN_SESSION WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Workout> getWorkout() {
        ArrayList<Workout> workouts = new ArrayList<>();
        String sql = "SELECT id,name FROM WORKOUT";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                workouts.add(new Workout(rs.getInt("id"),rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workouts;
    }

    public static void updateTrainingSession(TrainingSession updatedSession) {
        String sql = "UPDATE TRAIN_SESSION SET date = ?, workout_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedSession.getDate().toString());
            pstmt.setInt(2, updatedSession.getWorkout().getId());
            pstmt.setInt(3, updatedSession.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addWorkout(String name) {
        String sql = "INSERT INTO WORKOUT (name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteWorkout(int id) {
        String sql = "DELETE FROM WORKOUT WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteWorkoutinWE(int id) {
        String sql = "DELETE FROM WORKOUT_EXERCISE WHERE workout_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ExerciseDetail> getExerciseDetailsForWorkout(int workoutId) {
        ArrayList<ExerciseDetail> details = new ArrayList<>();
        String sql = """
    SELECT ed.exerciseDetail_id, ed.exercise_id, ed.sets, ed.value, ed.unit, e.name, e.type 
    FROM WORKOUT_EXERCISE we
    JOIN EXERCISE_DETAIL ed ON we.exercise_detail_id = ed.exerciseDetail_id
    JOIN EXERCISE e ON ed.exercise_id = e.id
    WHERE we.workout_id = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workoutId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Exercise ex = new Exercise(rs.getInt("exercise_id"), rs.getString("name"), rs.getString("type"));
                    details.add(new ExerciseDetail(rs.getInt("exerciseDetail_id"), ex, rs.getInt("sets"), rs.getString("unit"), rs.getFloat("value")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static void updateWorkout(int workoutId, String name) {
        String sql = "UPDATE WORKOUT SET name = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, workoutId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addExercisetoWorkout(int workoutId, int exerciseDetailId) {
        String checkSql = "SELECT COUNT(*) FROM WORKOUT_EXERCISE WHERE workout_id = ? AND exercise_detail_id = ?";
        String insertSql = "INSERT INTO WORKOUT_EXERCISE (workout_id, exercise_detail_id) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setInt(1, workoutId);
            checkStmt.setInt(2, exerciseDetailId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("To ćwiczenie jest już dodane do treningu.");
                    return;
                }
            }

            insertStmt.setInt(1, workoutId);
            insertStmt.setInt(2, exerciseDetailId);
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteExercisefromWorkout(int w_id, int ex_id) {
        String sql = "DELETE FROM WORKOUT_EXERCISE WHERE exercise_detail_id = ? AND workout_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ex_id);
            pstmt.setInt(2, w_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<TrainingSession> getTrainingSessionsForExercise(int exerciseId) {
        ArrayList<TrainingSession> sessions = new ArrayList<>();
        String sql = """
    SELECT ts.id, ts.date, w.id AS workout_id, w.name
    FROM TRAIN_SESSION ts
    JOIN WORKOUT w ON ts.workout_id = w.id
    JOIN WORKOUT_EXERCISE we ON w.id = we.workout_id
    JOIN EXERCISE_DETAIL ed ON we.exercise_detail_id = ed.exerciseDetail_id
    WHERE ed.exercise_id = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, exerciseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    Workout w = new Workout(rs.getInt("workout_id"), rs.getString("name"));
                    sessions.add(new TrainingSession(rs.getInt("id"), date, w));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public static float getValuesForExerciseOnDate(int exerciseId, LocalDate date) {
        float value = 0;
        String sql = """
    SELECT ed.value
    FROM EXERCISE_DETAIL ed
    JOIN WORKOUT_EXERCISE we ON we.exercise_detail_id = ed.exerciseDetail_id
    JOIN WORKOUT w  ON w.id = we.workout_id
    JOIN TRAIN_SESSION ts ON ts.workout_id = w.id
    WHERE ed.exercise_id = ? And ts.date = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, exerciseId);
            pstmt.setString(2, date.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    value =rs.getFloat("value");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

}