# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

Paper.create([
                 {owner: 1234, artifct: 1234, content_location: 'example.com/14234.md'},
                 {owner: 1234, artifct: 12345, content_location: 'example.com/12364.md'},
                 {owner: 1234, artifct: 12346, content_location: 'example.com/12354.md'},
                 {owner: 12324, artifct: 12343, content_location: 'example.com/12334.md'},

             ])
