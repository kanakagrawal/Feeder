# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-10-27 20:10
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('feeder', '0002_auto_20161026_1212'),
    ]

    operations = [
        migrations.AddField(
            model_name='student',
            name='admin',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='feeder.Admin'),
        ),
    ]