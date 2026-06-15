CREATE TABLE groups (
                        group_id   SERIAL PRIMARY KEY,
                        group_name VARCHAR(20) NOT NULL
);

CREATE TABLE courses (
                         course_id          SERIAL PRIMARY KEY,
                         course_name        VARCHAR(100) NOT NULL,
                         course_description TEXT
);

CREATE TABLE students (
                          student_id SERIAL PRIMARY KEY,
                          group_id   INTEGER REFERENCES groups(group_id) ON DELETE SET NULL,
                          first_name VARCHAR(50) NOT NULL,
                          last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE students_courses (
                                  student_id INTEGER NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
                                  course_id  INTEGER NOT NULL REFERENCES courses(course_id) ON DELETE CASCADE,
                                  PRIMARY KEY (student_id, course_id)
);