from django.conf.urls import url

from . import views

app_name = 'testreg'
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^newreg$', views.newreg, name='newreg'),
    url(r'^newapl$', views.newapl, name='newapl'),
    url(r'^listreg$', views.listreg, name='listreg'),
    url(r'^(?P<id>[0-9]+)/deletereg$', views.deletereg, name='deletereg'),
    url(r'^(?P<id>[0-9]+)/detail$', views.detail, name='detail'),
    url(r'^findreg$', views.findreg, name='findreg'),
]