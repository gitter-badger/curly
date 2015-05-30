Rails.application.routes.draw do
  get '/smuggle/:owner/:repo', to: 'smuggler#get_readme_info', :defaults => {:format => 'json'}
end
