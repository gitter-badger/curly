class PapersController < ApplicationController
  before_action :set_paper, only: [:update, :destroy]

  respond_to :json

  #GET /papers/artifact/:artifact/owner/:owner
  def get_by_owner_and_artifact
    @papers = Paper.all
  end

  # POST /papers
  def create
    @paper = Paper.new(paper_params)
    if @paper.save
      respond_with status: :created, location: @paper
    else
      respond_with render json: @paper.errors, status: :unprocessable_entity
    end
  end


  # PATCH/PUT /papers/1
  # PATCH/PUT /papers/1.json
  def update
    if @paper.update(paper_params)
      respond_with status: :created, location: @paper
    else
      respond_with render json: @paper.errors, status: :unprocessable_entity
    end
  end


  # DELETE /papers/1
  # DELETE /papers/1.json
  def destroy
    @paper.destroy
    respond_with status: :no_content
  end

  private
  # Use callbacks to share common setup or constraints between actions.
  def set_paper
    @paper = Paper.find(params[:id])
  end

  # Never trust parameters from the scary internet, only allow the white list through.
  def paper_params
    params[:paper]
  end
end
