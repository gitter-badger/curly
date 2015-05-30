class SmugglerController < ApplicationController

  def get_readme_info
    owner = params[:owner]
    repo = params[:repo]
    Octokit.readme "#{owner}/#{repo}", :accept => 'application/vnd.github.md'
  end

end
