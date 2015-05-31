#!/usr/bin/env bash

cd ../smuggler
bundle install
rake db:migrate
rails server

