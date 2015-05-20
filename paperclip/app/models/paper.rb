class Paper < ActiveRecord::Base

  validates :owner, :artifact, :content_location, :presence => true

end
