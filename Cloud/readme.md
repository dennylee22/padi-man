<h1 class="code-line" data-line-start=0 data-line-end=1 ><a id="Bangkit_2021_Cloud_Computing_Path_0"></a>Bangkit 2021 Cloud Computing Path</h1>
<h3 class="code-line" data-line-start=1 data-line-end=2 ><a id="Denny_Lee__C0050428__Steven_Christian__C0050410_1"></a>Denny Lee - C0050428 / Steven Christian - C0050410</h3>
<p class="has-line-data" data-line-start="2" data-line-end="3">This API is build to serve the mobile application called “Padi-man”. This API is build using Python 3 and uses the RESTful architecture with the “flask_restful” python module. Then the API is deployed to an Apache webserver.</p>
<h2 class="code-line" data-line-start=3 data-line-end=4 ><a id="Features_3"></a>Features</h2>
<ul>
<li class="has-line-data" data-line-start="5" data-line-end="6">Transfers the image sent by the user to the machine learning model</li>
<li class="has-line-data" data-line-start="6" data-line-end="7">Transfers the prediction results from the machine learning model back to the user.</li>
<li class="has-line-data" data-line-start="7" data-line-end="8">Everytime a disease is detected, the API will query the database from the SQL instance for the remedies and send it to the user.</li>
<li class="has-line-data" data-line-start="8" data-line-end="10">Saves the date and the name of the disease detected to BigQuery for data warehousing.</li>
</ul>
<p class="has-line-data" data-line-start="10" data-line-end="11">API endpoints:</p>
<ul>
<li class="has-line-data" data-line-start="11" data-line-end="12">POST <a href="http://34.126.165.65/upload">http://34.126.165.65/upload</a> (Public IP)</li>
<li class="has-line-data" data-line-start="12" data-line-end="13">GET <a href="http://34.126.165.65/list">http://34.126.165.65/list</a> (Public IP)</li>
<li class="has-line-data" data-line-start="13" data-line-end="14">POST <a href="http://192.168.1.5/model">http://192.168.1.5/model</a> (Private IP)</li>
</ul>
