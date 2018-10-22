import { Component, OnInit, AfterContentInit, ViewChild, ElementRef } from '@angular/core';
import * as d3 from 'd3';
import { Spring } from '../spring';
import { DataModelService } from '../data-model.service';
import { Scenario } from '../scenario';

@Component({
  selector: 'app-spring-connection-chart',
  templateUrl: './spring-connection-chart.component.html',
  styleUrls: ['./spring-connection-chart.component.css']
})
export class SpringConnectionChartComponent implements OnInit, AfterContentInit {
  
  @ViewChild('chart')
  chartElement: ElementRef;

  spring: Spring;
  scenario: Scenario;

  constructor(
    private dataModelService: DataModelService
  ) { }

  ngOnInit() {
    this.dataModelService.scenario$.subscribe(s => {
      this.scenario = s;
      this.buildChart();
    });
    this.dataModelService.selectedSpring$.subscribe(sp => {
      this.spring = sp;
      this.buildChart();
    });
  }

  ngAfterContentInit() {

  }

  buildChart()  {
    if (this.spring) {
      // Standard Margin Convention
      var margin = {top: 20, right: 10, bottom: 20, left: 30};
      var width = 300 - margin.left - margin.right;
      var height = 140 - margin.top - margin.bottom;

      // Scales
      var dataPaddingFactor = 0.1;
      var domXMin = Math.floor((1-dataPaddingFactor) * d3.min([this.scenario.mAllowedRangeR2MillimetersMin, this.spring.mSpringCPMin]));
      var domXMax = Math.ceil((1+dataPaddingFactor) * d3.max([this.scenario.mAllowedRangeR2MillimetersMax, this.spring.mSpringCPMax]));
      var domYMin = Math.floor((1-dataPaddingFactor) * d3.min([this.scenario.mAllowedRangeAMillimetersMin, this.spring.mSpringCPMin]));
      var domYMax = Math.ceil((1+dataPaddingFactor) * d3.max([this.scenario.mAllowedRangeAMillimetersMax, this.spring.mSpringCPMax]));

      var xScale = d3.scaleLinear()
        .domain([domXMin, domXMax])
        .range([0,width]);
      var yScale = d3.scaleLinear()
        .domain([domYMin, domYMax])
        .range([height, 0])

      // Generate the line for the region
      var numLinePoints = 20;
      let _this = this;
      var lineData = d3.range(numLinePoints).map(function(d) {
        var xValue = _this.spring.mSpringCPMin + (_this.spring.mSpringCPMax - _this.spring.mSpringCPMin)/numLinePoints*d;
        return [
          xValue,
          _this.spring.mMaxPayloadAnchorPointFactor/xValue
        ]
      });
      var line = d3.line()
          .x(function(d) { return xScale(d[0]); })
          .y(function(d) { return yScale(d[1]); })
          .curve(d3.curveMonotoneX); // Smooth out the line
      
      // Delete the existing if there is one and create a new one
      d3.select(this.chartElement.nativeElement).select('svg').remove();

      // Set up the SVG element
      var svg = d3.select(this.chartElement.nativeElement).append('svg')
        .attr('width', width + margin.left + margin.right)
        .attr('height', height + margin.top + margin.bottom)
        .append('g')
        .attr('transform', `translate(${margin.left},${margin.top})`);

      // Create the axes
      svg.append("g")
          .attr("class", "x axis")
          .attr("transform", "translate(0," + height + ")")
          .call(d3.axisBottom(xScale));
      svg.append("g")
          .attr("class", "y axis")
          .call(d3.axisLeft(yScale));

      // Append the spring curve
      svg.append("path")
          .datum(lineData)
          .attr("class", "spring-connection-chart-line")
          .attr("d", line);
      // Append the connection regions
      svg.append("rect")
          .attr("class", "spring-connection-chart-spring-rect")
          .attr("x", xScale(_this.spring.mSpringCPMin))
          .attr("y", yScale(_this.spring.mSpringCPMax))
          .attr("width", xScale(_this.spring.mSpringCPMax) - xScale(_this.spring.mSpringCPMin))
          .attr("height", yScale(_this.spring.mSpringCPMin) - yScale(_this.spring.mSpringCPMax));
      svg.append("rect")
          .attr("class", "spring-connection-chart-allowed-rect")
          .attr("x", xScale(_this.scenario.mAllowedRangeR2MillimetersMin))
          .attr("y", yScale(_this.scenario.mAllowedRangeAMillimetersMax))
          .attr("width", xScale(_this.scenario.mAllowedRangeR2MillimetersMax) - xScale(_this.scenario.mAllowedRangeR2MillimetersMin))
          .attr("height", yScale(_this.scenario.mAllowedRangeAMillimetersMin) - yScale(_this.scenario.mAllowedRangeAMillimetersMax));

    }
  }

}
