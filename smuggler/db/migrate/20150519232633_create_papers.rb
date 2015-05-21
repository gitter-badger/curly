class CreatePapers < ActiveRecord::Migration
  def change
    create_table :papers do |t|
      t.owner
      t.artifact
      t.content_location
      t.timestamps null: false
    end
  end
end
