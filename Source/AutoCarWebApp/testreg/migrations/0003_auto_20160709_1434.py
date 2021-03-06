# -*- coding: utf-8 -*-
# Generated by Django 1.9.7 on 2016-07-09 13:34
from __future__ import unicode_literals

import datetime
from django.db import migrations, models
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('testreg', '0002_auto_20160704_1842'),
    ]

    operations = [
        migrations.RenameField(
            model_name='applicant',
            old_name='TestID',
            new_name='BiometricID',
        ),
        migrations.RenameField(
            model_name='test',
            old_name='TestID',
            new_name='applicant',
        ),
        migrations.RemoveField(
            model_name='test',
            name='TestData',
        ),
        migrations.AddField(
            model_name='test',
            name='TestDate',
            field=models.DateField(default=datetime.datetime(2016, 7, 9, 13, 34, 33, 580401, tzinfo=utc), verbose_name='Test date'),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='applicant',
            name='DateOfBirth',
            field=models.DateField(verbose_name='birth date'),
        ),
        migrations.AlterField(
            model_name='applicant',
            name='IDType',
            field=models.CharField(choices=[('A', 'Aadhaar'), ('P', 'PAN'), ('O', 'Other')], max_length=1),
        ),
    ]
