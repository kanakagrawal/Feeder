# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-10-31 12:30
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('feeder', '0013_admin_loggedin'),
    ]

    operations = [
        migrations.AddField(
            model_name='instructor',
            name='loggedin',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='student',
            name='loggedin',
            field=models.BooleanField(default=False),
        ),
    ]