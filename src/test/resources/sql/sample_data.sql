INSERT INTO groups(group_id, group_name) VALUES
    (1, 'AA-11'),
    (2, 'BB-22'),
    (3, 'CC-33');

INSERT INTO courses(course_id, course_name, course_description) VALUES
    (1, 'Math', 'Numbers and equations'),
    (2, 'Biology', 'Living organisms'),
    (3, 'Physics', 'Forces and energy');

INSERT INTO students(student_id, group_id, first_name, last_name) VALUES
    (1, 1, 'John', 'Doe'),
    (2, 1, 'Jane', 'Roe'),
    (3, 2, 'Max', 'Mux'),
    (4, NULL, 'Lone', 'Wolf');

INSERT INTO students_courses(student_id, course_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (3, 3);

-- Keep SERIAL sequences ahead of the explicit IDs above so new inserts don't collide.
SELECT setval('groups_group_id_seq', (SELECT MAX(group_id) FROM groups));
SELECT setval('courses_course_id_seq', (SELECT MAX(course_id) FROM courses));
SELECT setval('students_student_id_seq', (SELECT MAX(student_id) FROM students));