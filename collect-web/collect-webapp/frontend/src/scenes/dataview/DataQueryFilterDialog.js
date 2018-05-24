import React, { Component } from 'react'
import Button from '@material-ui/core/Button'
import Checkbox from '@material-ui/core/Checkbox'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogContentText from '@material-ui/core/DialogContentText'
import DialogTitle from '@material-ui/core/DialogTitle'
import Input from '@material-ui/core/Input'
import InputLabel from '@material-ui/core/InputLabel'
import FormControl from '@material-ui/core/FormControl'
import FormHelperText from '@material-ui/core/FormHelperText'
import List from '@material-ui/core/List'
import ListItem from '@material-ui/core/ListItem'
import ListItemIcon from '@material-ui/core/ListItemIcon'
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction'
import ListSubheader from '@material-ui/core/ListSubheader'
import ListItemText from '@material-ui/core/ListItemText'
import TextField from '@material-ui/core/TextField'
import LeftArrowIcon from '@material-ui/icons/ChevronLeft'
import RightArrowIcon from '@material-ui/icons/ChevronRight'
import FastForwardIcon from '@material-ui/icons/FastForward'
import FastRewindIcon from '@material-ui/icons/FastRewind'

import { FilterCondition } from 'model/DataQuery'
import ServiceFactory from 'services/ServiceFactory'
import L from 'utils/Labels'
import Arrays from 'utils/Arrays'

export default class DataQueryFilterDialog extends Component {

    constructor(props) {
        super(props)

        this.state = {
            queryComponent: null,
            availableMembersLoaded: false,
            filterType: null,
            availableMembers: [], 
            selectedAvailableMembers: [],
            usedMembers: [],
            selectedUsedMembers: []
        }
        this.loadAvailableMembers = this.loadAvailableMembers.bind(this)
        this.availableMembersLoaded = this.availableMembersLoaded.bind(this)
        this.addOrRemoveElement = this.addOrRemoveElement.bind(this)
        this.extractQueryCondition = this.extractQueryCondition.bind(this)
        this.handleOk = this.handleOk.bind(this)
        this.handleAddSelectedMembersClick = this.handleAddSelectedMembersClick.bind(this)
        this.handleAddAllMembersClick = this.handleAddAllMembersClick.bind(this)
        this.handleRemoveSelectedUsedMembersClick = this.handleRemoveSelectedUsedMembersClick.bind(this)
        this.handleRemoveAllUsedMembersClick = this.handleRemoveAllUsedMembersClick.bind(this)
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        const { open, queryComponent } = nextProps
        if (!open || !queryComponent) {
            return null
        }
        const attrDef = queryComponent.attributeDefinition
        const filterCondition = queryComponent.filterCondition
        let filterType, value, minValue, maxValue

        switch(attrDef.attributeType) {
            case 'BOOLEAN':
            case 'CODE':
            case 'TAXON':
                filterType = 'IN'
                break
            case 'DATE':
            case 'NUMBER':
            case 'TIME':
                filterType = 'BETWEEN'
                if (filterCondition) {
                    minValue = filterCondition.min
                    maxValue = filterCondition.max
                }
                break
            case 'TEXT':
                filterType = 'CONTAINS'
                if (filterCondition) {
                    value = filterCondition.value
                }
                break

        }
        return {
            queryComponent: queryComponent,
            availableMembersLoaded: false,
            availableMembers: [],
            selectedUsedMembers: [],
            filterType: filterType,
            selectedAvailableMembers: [],
            selectedUsedMembers: [],
            minValue: minValue,
            maxValue: maxValue,
            value: value
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        const { availableMembersLoaded } = this.state
        const { queryComponent } = this.props
    
        if (queryComponent && ! availableMembersLoaded) {
            this.loadAvailableMembers(queryComponent)
        }
    }

    loadAvailableMembers(queryComponent) {
        const attrDef = queryComponent.attributeDefinition
        const survey = attrDef.survey
        const codeListId = attrDef.codeListId

        switch(queryComponent.attributeDefinition.attributeType) {
        case 'BOOLEAN':
            this.availableMembersLoaded([
                {
                    code: 'true', 
                    label: "True"
                }, 
                {
                    code: 'false',
                    label: "False"
                }
            ])
            break
        case 'CODE':
            ServiceFactory.codeListService.findAllItems(survey, codeListId, attrDef.codeListLevel).then(items =>
                this.availableMembersLoaded(items)
            )
            break
        case 'TAXON':
            //TODO
            this.availableMembersLoaded([{label: 'ACA'}, {label: 'AFA'}])
            break
        default:
            this.availableMembersLoaded()
        }
    }

    availableMembersLoaded(values) {
        const { queryComponent } = this.state
        const filterCondition = queryComponent.filterCondition
        const availableMembers = []
        const usedMembers = []

        if (values) {
            values.forEach(v => {
                if (filterCondition && filterCondition.values && 
                        Arrays.contains(filterCondition.values, v, 'code')) {
                    usedMembers.push(v)
                } else {
                    availableMembers.push(v)
                }
            })
        }

        this.setState({
            availableMembersLoaded: true,
            availableMembers: availableMembers,
            usedMembers: usedMembers
        })
    }

    handleToggleAvailableMember(value) {
        const newSelected = this.addOrRemoveElement(this.state.selectedAvailableMembers, value)
        this.setState({
            selectedAvailableMembers: newSelected,
        })
    }

    handleToggleUsedMember(value) {
        const newSelected = this.addOrRemoveElement(this.state.selectedUsedMembers, value)
        this.setState({
            selectedUsedMembers: newSelected,
        })
    }

    addOrRemoveElement(list, el) {
        const newList = [...list]
        const currentIndex = list.indexOf(el)

        if (currentIndex === -1) {
            newList.push(el)
        } else {
            newList.splice(currentIndex, 1)
        }
        return newList
    }

    handleAddSelectedMembersClick() {
        const newAvailableMembers = Arrays.removeItems(this.state.availableMembers, this.state.selectedAvailableMembers)
        const newUsedMembers = Arrays.addItems(this.state.usedMembers, this.state.selectedAvailableMembers)
        this.setState({
            availableMembers: newAvailableMembers,
            usedMembers: newUsedMembers,
            selectedAvailableMembers: []
        })
    }

    handleAddAllMembersClick() {
        const newUsedMembers = Arrays.addItems(this.state.usedMembers, this.state.availableMembers)
        this.setState({
            availableMembers: [],
            usedMembers: newUsedMembers,
            selectedAvailableMembers: []
        })
    }

    handleRemoveSelectedUsedMembersClick() {
        const newAvailableMembers = Arrays.addItems(this.state.availableMembers, this.state.selectedUsedMembers)
        const newUsedMembers = Arrays.removeItems(this.state.usedMembers, this.state.selectedUsedMembers)
        this.setState({
            availableMembers: newAvailableMembers,
            usedMembers: newUsedMembers,
            selectedUsedMembers: []
        })
    }

    handleRemoveAllUsedMembersClick() {
        const newAvailableMembers = Arrays.addItems(this.state.availableMembers, this.state.usedMembers)
        this.setState({
            availableMembers: newAvailableMembers,
            selectedUsedMembers: [],
            usedMembers: []
        })
    }

    handleOk() {
        const queryCondition = this.extractQueryCondition()
        this.props.onOk(queryCondition)
    }

    extractQueryCondition() {
        const condition = new FilterCondition()
        condition.type = this.state.filterType

        switch(this.state.filterType) {
            case 'IN':
                condition.values = this.state.usedMembers
                break
            case 'BETWEEN':
                condition.min = this.state.minValue
                condition.max = this.state.maxValue
                break
            case 'CONTAINS':
                condition.value = this.state.value
        }
        return condition.empty ? null : condition
    }

    render() {
        const { open, onClose, queryComponent } = this.props
        const { availableMembersLoaded, filterType, availableMembers, usedMembers, selectedAvailableMembers, selectedUsedMembers,
            value, minValue, maxValue } = this.state

        return (
            <Dialog onClose={onClose} open={open} maxWidth="850px">
                <DialogTitle id="simple-dialog-title">Filter</DialogTitle>
                {! availableMembersLoaded && 
                    <div>Loading...</div>
                }
                {availableMembersLoaded && filterType === 'IN' && 
                    <div style={{width: 800}} className="row">
                        <div className="col-md-6">
                            <List subheader={<ListSubheader>Available Members</ListSubheader>} 
                                    style={{maxHeight: 300, maxWidth: 350, overflow: 'auto'}}>
                                {availableMembers.map(v => (
                                    <ListItem
                                        key={v}
                                        role={undefined}
                                        dense
                                        button
                                        onClick={this.handleToggleAvailableMember.bind(this, v)}>
                                        <Checkbox
                                            checked={selectedAvailableMembers.indexOf(v) !== -1}
                                            tabIndex={-1}
                                            disableRipple />
                                        <ListItemText primary={`${v.label} [${v.code}]`} />
                                    </ListItem>
                                ))}
                            </List>
                        </div>
                        <div className="col-md-1">
                            <Button variant="fab" mini color="secondary" aria-label="add members"
                                disabled={selectedAvailableMembers.length === 0}
                                onClick={this.handleAddSelectedMembersClick}>
                                <RightArrowIcon />
                            </Button>
                            <Button variant="fab" mini color="secondary" aria-label="add all members"
                                disabled={availableMembers.length === 0}
                                onClick={this.handleAddAllMembersClick}>
                                <FastForwardIcon />
                            </Button>
                            <Button variant="fab" mini color="secondary" aria-label="remove used members"
                                disabled={selectedUsedMembers.length === 0}
                                onClick={this.handleRemoveSelectedUsedMembersClick}>
                                <LeftArrowIcon />
                            </Button>
                            <Button variant="fab" mini color="secondary" aria-label="remove all used members"
                                disabled={usedMembers.length === 0}
                                onClick={this.handleRemoveAllUsedMembersClick}>
                                <FastRewindIcon />
                            </Button>
                        </div>
                        <div className="col-md-5">
                            <List subheader={<ListSubheader>Used Members</ListSubheader>}
                                    style={{maxHeight: 300, maxWidth: 350, overflow: 'auto'}}>
                                {usedMembers.map(v => (
                                    <ListItem
                                        key={v}
                                        role={undefined}
                                        dense
                                        button
                                        onClick={this.handleToggleUsedMember.bind(this, v)}>
                                        <Checkbox
                                            checked={selectedUsedMembers.indexOf(v) !== -1}
                                            tabIndex={-1}
                                            disableRipple />
                                        <ListItemText primary={`${v.label} [${v.code}]`} />
                                    </ListItem>
                                ))}
                            </List>
                        </div>
                    </div>
                }
                {availableMembersLoaded && filterType === 'BETWEEN' && 
                    <div>
                        <div className="col-md-6">
                            <TextField
                                label={L.l('dataView.query.condition.minValue')}
                                value={this.state.minValue}
                                onChange={e => this.setState({minValue: e.target.value})}
                                margin="normal"
                            />
                        </div>
                        <div className="col-md-6">
                            <TextField
                                label={L.l('dataView.query.condition.maxValue')}
                                value={this.state.maxValue}
                                onChange={e => this.setState({maxValue: e.target.value})}
                                margin="normal"
                            />
                        </div>
                    </div>
                }
                {availableMembersLoaded && filterType === 'CONTAINS' && 
                    <div>
                        <div className="col-md-12">
                            <TextField
                                label={L.l('dataView.query.condition.contains')}
                                value={this.state.value}
                                onChange={e => this.setState({value: e.target.value})}
                                margin="normal"
                            />
                        </div>
                    </div>
                }
                <DialogActions>
                    <Button onClick={this.props.onClose}>
                        {L.l('general.cancel')}
                    </Button>
                    <Button onClick={this.handleOk} color="primary" variant="raised" disabled={!availableMembersLoaded}>
                        {L.l('general.ok')}
                    </Button>
                </DialogActions>
            </Dialog>
        )
    }

}
