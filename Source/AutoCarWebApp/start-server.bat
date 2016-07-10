@ECHO OFF
START python manage.py makemigrations
START python manage.py migrate
START python manage.py runserver 0.0.0.0:8000
