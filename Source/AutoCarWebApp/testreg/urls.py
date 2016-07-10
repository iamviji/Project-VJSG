from django.conf.urls import url

from . import views

app_name = 'testreg'
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^newreg$', views.newreg, name='newreg'),
    url(r'^newapl$', views.newapl, name='newapl'),
    url(r'^listreg$', views.listreg, name='listreg'),
    url(r'^(?P<id>[0-9]+)/deletereg$', views.deletereg, name='deletereg'),
    url(r'^(?P<id>[0-9]+)/updatereg$', views.updatereg, name='updatereg'),
    url(r'^(?P<id>[0-9]+)/modifyreg$', views.modifyreg, name='modifyreg'),
    url(r'^(?P<id>[0-9]+)/detail$', views.detail, name='detail'),
    url(r'^findreg$', views.findreg, name='findreg'),
    url(r'^searchreg', views.searchreg, name='searchreg'),
    url(r'^(?P<id>[0-9]+)/get_testdata', views.get_testdata, name='get_testdata'),
    url(r'^(?P<id>[0-9]+)/save_testdata', views.save_testdata, name='save_testdata'),
]