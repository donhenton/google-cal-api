import * as d3 from 'd3';
import * as d3_scale from 'd3-scale';


export default class HorizontalBarChart {

    /**
     * el    -- element reference to the containing HTML element eg $('#myThing')[0]
     * props -- {width: 200, height: 400}
     * id    -- stting that will be assigned as an id to the svg element
     * data  -- array of objects {label: xxxx, value: yyyy (0-100 simple percentage type number), color: #ef2393 }
     */

    constructor(el, props, id, data)
    {

        this.svg = null;
        this.element = el;
        this.props = props;
        this.context = id;
        this.intervals = [];
        this.keyfunction = function (d) {
            return d.color
        };
        this.createChart(data);



    }

    createChart(data)
    {
        let me = this;
        this.svg = d3.select(me.element).append('svg')
                .attr('id', me.context)
                .attr('width', me.props.width+10)
                .style('background-color', 'white')
                .style('border', 'thin solid black')
                .style('padding','5px')
                .attr('height', me.props.height+10);

        this.svg.append('defs')
                .append('pattern')
                .attr('id', 'diagonalStripes')
                .attr('patternUnits', 'userSpaceOnUse')
                .attr('width', 4)
                .attr('height', 4)
                .append('path')
                .attr('d', 'M-1,1 l2,-2 M0,4 l4,-4 M3,5 l2,-2')
                .attr('stroke', '#c7d2d8')
                .attr('stroke-width', 1);


        this.build(data);

    }

    build(data)
    {
        let self = this;

        this.xScale = d3_scale.scaleLinear()
                .domain([0, 100])
                .range([0, this.props.width]);
        //set the height of the horoxaontal bar

        //if you want to have a max size based on the height of the bars with a data set with 2 entries
        this.barHeight = data.length < 2 ? this.props.height / 2 : this.props.height / data.length;

        self.groups = d3.select('#' + self.context)
                .selectAll('g.group-content')
                .data(data, this.keyfunction);
        //enter statement
        self.groupsEnter = self.groups
                .enter()
                .append('g')
                .attr('class', 'group-content')
        //groups for each item
        self.contentGroups = self.groupsEnter
                .append('svg:g')
                .attr('class', 'rect-content')
                .attr(
                        'transform', function (d, i) {
                            return "translate(0," + i * self.barHeight + ")";

                        });
        //initial background bars / striped background
        self.contentGroups
                .append('rect')
                .attr('class', 'background-bar')
                .attr('width', self.props.width)
                .attr('height', (self.barHeight - 22))
                .attr('transform', "translate(0,23)")
                .attr('fill', 'url(#diagonalStripes)')
                .style('opacity', 1);





        //actual colored bars of the bar chart
        self.contentGroups
                .append('rect')
                .attr('class', 'bar')
                .style('fill', function (d, i) {
                    return d.color;
                })
                .style('opacity', 1)
                .attr('transform', 'translate(0,23)')
                .attr('height', (self.barHeight - 22))
                .attr('width',0)
                .transition()
                .duration(500)
                .delay(100)
                .attr(
                        'width', function (d, i) {
                            return self.xScale(d.percentage);
                        })
                
                

        //name of each bar
        self.contentGroups
                .append("text")
                .attr('class', 'label')
                .attr('transform', "translate(0,18)")
                .style('font-family', "'graphikRegular', Helvetica, Arial, sans-serif")
                .style('fill', "#4d4e54")
                .style('font-size', '13px')
                .style('opacity', 1)
                .text(function (d) {
                    return d.name;
                });

        //percent of bar
        self.contentGroups
                .append("text")
                .attr('class', 'percent')
                .attr('text-anchor', 'end')
                .attr('transform', function (d, i) {
                    return "translate(" + self.props.width + ",18)";
                })
                .style('font-family', "'graphikRegular', Helvetica, Arial, sans-serif")
                .style('fill', "#4d4e54")
                .style('font-size', '13px')
                .style('opacity', 1)
                .text(function (d) {
                    return d.percentage + '%';
                });

        self.groups.exit().remove();
////////////////////////
        let rectGroupsTransition = self.groups.transition();
        // .selectAll('.rect-content')

        rectGroupsTransition
                .select('.rect-content')
                .attr('transform', function (d, i) {
                    return "translate(0," + i * self.barHeight + ")";
                });

        rectGroupsTransition
                .select('.bar')
                .attr('transform', 'translate(0,23)')
                .attr('height', (self.barHeight - 22))
                .attr('width',0)
                .transition()
                .duration(500)
                .delay(100)
                .attr(
                        'width', function (d, i) {
                            return self.xScale(d.percentage);
                        })
                
        rectGroupsTransition
                .select('.background-bar')
                .attr('height', (self.barHeight - 22))
 
        rectGroupsTransition
                .select('.percent')
                .attr('transform', function () {
                    return "translate(" + self.props.width + ",18)";
                })
                .attr('text-anchor', 'end')
                .text(function (d) {
                    return d.percentage == 0.5 ? '<0.5%' : d.percentage + '%';
                });

        rectGroupsTransition
                .select('.label')
                .attr('transform', 'translate(0,18)')
                .attr('title', function (d, i) {
                    return d.name;
                })
                 
                .text(function (d) {
                     return d.name
                });


    }

 
}



