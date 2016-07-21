@ECHO OFF
call workon autorto
call python manage.py makemigrations
call python manage.py migrate
call python manage.py runserver 0.0.0.0:8000
