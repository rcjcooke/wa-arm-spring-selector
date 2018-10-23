import { Component, OnInit, AfterContentInit, ViewChild, ElementRef, Input } from '@angular/core';
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

  // Component attributes
  @Input() chartWidth: number;
  @Input() chartHeight: number;

  bigMode: boolean = false;

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
    if (this.chartWidth >= 500 && this.chartHeight >= 300) {
      this.bigMode = true;
    }
    this.buildChart();
  }

  buildChart()  {
    if (this.spring) {
      // Standard Margin Convention
      var margin = {top: 20, right: 10, bottom: 20, left: 30};
      var width = this.chartWidth - margin.left - margin.right;
      var height = this.chartHeight - margin.top - margin.bottom;

      // Scales
      var dataPaddingFactor = 0.1;
      var domXRange = d3.max([this.scenario.mAllowedRangeR2MillimetersMax, this.spring.mSpringCPMax]) - d3.min([this.scenario.mAllowedRangeR2MillimetersMin, this.spring.mSpringCPMin])
      var domYRange = d3.max([this.scenario.mAllowedRangeAMillimetersMax, this.spring.mSpringCPMax]) - d3.min([this.scenario.mAllowedRangeAMillimetersMin, this.spring.mSpringCPMin])
      var domXMin = d3.max([Math.floor(d3.min([this.scenario.mAllowedRangeR2MillimetersMin, this.spring.mSpringCPMin]) - domXRange * dataPaddingFactor),0]);
      var domXMax = Math.ceil(d3.max([this.scenario.mAllowedRangeR2MillimetersMax, this.spring.mSpringCPMax]) + domXRange * dataPaddingFactor);
      var domYMin = d3.max([Math.floor(d3.min([this.scenario.mAllowedRangeAMillimetersMin, this.spring.mSpringCPMin]) - domYRange * dataPaddingFactor),0]);
      var domYMax = Math.ceil(d3.max([this.scenario.mAllowedRangeAMillimetersMax, this.spring.mSpringCPMax]) + domYRange * dataPaddingFactor);

      var xScale = d3.scaleLinear()
        .domain([domXMin, domXMax])
        .range([0,width]);
      var yScale = d3.scaleLinear()
        .domain([domYMin, domYMax])
        .range([height, 0])

      // Generate the line for the region
      var numLinePoints = this.bigMode ? 100 : 20;
      let _this = this;
      var lineData = d3.range(numLinePoints+1).map(function(d) {
        var xValue = _this.spring.mSpringCPMin + (_this.spring.mSpringCPMax - _this.spring.mSpringCPMin)/numLinePoints*d;
        return [
          xValue,
          _this.spring.mMaxPayloadAnchorPointFactor/xValue
        ]
      });
      var line = d3.line()
        .x(function(d) { return xScale(d[0]); })
        .y(function(d) { return yScale(d[1]); })
        .curve(d3.curveCatmullRom.alpha(0.5)); // Smooth out the line
      
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
      if (this.bigMode) {
        // Add titles to the axes
        svg.append("text")
          .attr("class", "spring-connection-chart-axis-label")
          .attr("text-anchor", "end")
          .attr("transform", "translate("+ width +","+(height-(margin.bottom/3))+")")
          .text("R2 / mm");
        svg.append("text")
          .attr("class", "spring-connection-chart-axis-label")
          .attr("text-anchor", "end")
          .attr("transform", "translate("+ (margin.left/2) +",0)rotate(-90)")
          .text("A / mm");
      }
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
      if (this.bigMode) {
        svg.append("rect")
          .attr("class", "spring-connection-chart-valid-rect")
          .attr("x", xScale(_this.spring.mR2Min))
          .attr("y", yScale(_this.spring.mAMax))
          .attr("width", xScale(_this.spring.mR2Max) - xScale(_this.spring.mR2Min))
          .attr("height", yScale(_this.spring.mAMin) - yScale(_this.spring.mAMax));
      }

      // Append the optimum connection point marker
      if (this.bigMode) {
        svg.append("circle")
          .attr("class", "spring-connection-chart-optimum-connection-point")
          .attr("cx", xScale(_this.spring.mMaxPayloadAnchorPointFactor/_this.spring.mOptimumConnectionPointA))
          .attr("cy", yScale(_this.spring.mOptimumConnectionPointA))
          .attr("r", '2')
      }

    }
  }

}
